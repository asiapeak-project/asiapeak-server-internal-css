package com.asiapeak.server.internal.css.core.user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiapeak.server.internal.css.dao.entity.Users;
import com.asiapeak.server.internal.css.dao.enums.UserRole;
import com.asiapeak.server.internal.css.dao.repo.UsersRepo;

@Service
public class UserAuthService {

	@Autowired
	HttpServletRequest request;

	@Autowired
	UsersRepo usersRepo;

	@Value("${com.asiapeak.server.internal.css.admin.allows:}")
	String adminAllows;

	public void doLogout() {
		SecurityContextHolder.getContext().setAuthentication(null);
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}

	@Transactional
	public boolean isAdminCreated() {
		long count = usersRepo.countByRole(UserRole.ADMIN);

		if (Long.compare(count, 0L) != 0) {
			return true;
		} else {
			return false;
		}
	}

	@Transactional
	public boolean doLogin(String account, String password) {

		String hashed = OneWayHash.SHA512(password);

		Optional<Users> oUser = usersRepo.findByAccountAndPassword(account, hashed);

		if (!oUser.isPresent()) {
			return false;
		}

		List<GrantedAuthority> roles;

		Users users = oUser.get();

		if (users.getRole() == UserRole.ADMIN) {

			roles = Arrays.asList(UserAuthEnum.values()).stream().map(a -> {
				GrantedAuthority role = new SimpleGrantedAuthority(a.toString());
				return role;
			}).collect(Collectors.toList());

		} else {
			roles = users.getUsersAuths().stream().map(a -> {
				GrantedAuthority role = new SimpleGrantedAuthority(a.getAuthName());
				return role;
			}).collect(Collectors.toList());
		}
		UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(users.getAccount(), null, roles);
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);

		return true;
	}

	@Transactional
	public boolean createAdmin(String account, String password) {

		if (isAdminCreated()) {
			return false;
		}

		String hashed = OneWayHash.SHA512(password);

		Users users = new Users();

		users.setAccount(account);
		users.setPassword(hashed);
		users.setRole(UserRole.ADMIN);

		usersRepo.save(users);

		return true;
	}

	public String getCurrentUserName() {
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			return null;
		}
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

	private static final ThreadLocal<List<String>> threadAuthRoles = new ThreadLocal<List<String>>() {
		@Override
		protected List<String> initialValue() {
			return null;
		}
	};

	public List<String> getCurrentAuthRoles() {
		if (threadAuthRoles.get() == null) {
			if (SecurityContextHolder.getContext().getAuthentication() == null) {
				threadAuthRoles.set(new ArrayList<>());
			} else {
				threadAuthRoles.set(SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().map(a -> {
					return a.getAuthority();
				}).collect(Collectors.toList()));
			}
		}
		return threadAuthRoles.get();
	}

}
