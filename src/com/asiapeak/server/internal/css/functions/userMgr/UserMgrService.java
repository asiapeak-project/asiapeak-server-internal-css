package com.asiapeak.server.internal.css.functions.userMgr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiapeak.server.internal.css.core.user.OneWayHash;
import com.asiapeak.server.internal.css.core.user.UserAuthEnum;
import com.asiapeak.server.internal.css.dao.entity.Users;
import com.asiapeak.server.internal.css.dao.entity.UsersAuth;
import com.asiapeak.server.internal.css.dao.enums.UserRole;
import com.asiapeak.server.internal.css.dao.repo.UsersAuthRepo;
import com.asiapeak.server.internal.css.dao.repo.UsersRepo;
import com.asiapeak.server.internal.css.functions.userMgr.dto.UserAuthItemOutputDto;
import com.asiapeak.server.internal.css.functions.userMgr.dto.UserAuthOutputDto;
import com.asiapeak.server.internal.css.functions.userMgr.dto.UsersOutputDto;

@Service
public class UserMgrService {

	@Autowired
	UsersRepo usersRepo;

	@Autowired
	UsersAuthRepo usersAuthRepo;

	@Transactional
	public List<UsersOutputDto> qryUsers() {
		return usersRepo.findByRole(UserRole.USER).stream().map(u -> {
			UsersOutputDto dto = new UsersOutputDto();
			dto.setRowid(u.getRowid());
			dto.setAccount(u.getAccount());
			dto.setHasTotp(StringUtils.isNoneBlank(u.getSecret()));
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String createUser(String account, String password) {
		if (usersRepo.findByAccount(account.toLowerCase()).isPresent()) {
			return "帳號已存在";
		}
		String hashed = OneWayHash.SHA512(password);

		Users users = new Users();

		users.setAccount(account.toLowerCase());
		users.setPassword(hashed);
		users.setRole(UserRole.USER);

		usersRepo.save(users);

		return null;
	}

	@Transactional
	public List<UserAuthOutputDto> qryAuth(Integer rowid) {

		Users user = usersRepo.findById(rowid).orElse(null);

		if (user == null) {
			return new ArrayList<>();
		}

		List<String> authed = user.getUsersAuths().stream().map(a -> a.getAuthName()).collect(Collectors.toList());

		List<UserAuthOutputDto> list = Arrays.asList(UserAuthEnum.values()).stream().collect(Collectors.groupingBy(UserAuthEnum::getCategory)).entrySet().stream().map((entry) -> {
			String key = entry.getKey();
			List<UserAuthEnum> val = entry.getValue();
			UserAuthOutputDto dto = new UserAuthOutputDto();
			dto.setCategory(key);
			dto.setAuths(val.stream().sorted((i1, i2) -> i1.getSeq().compareTo(i2.getSeq())).map(i -> {
				UserAuthItemOutputDto item = new UserAuthItemOutputDto();
				item.setCode(i.toString());
				item.setDesc(i.getDesc());
				item.setEnabled(authed.contains(i.toString()));
				return item;
			}).collect(Collectors.toList()));
			return dto;
		}).sorted((a1, a2) -> {
			return Integer.compare(UserAuthEnum.CATEGORY_ORDER.indexOf(a1.getCategory()), UserAuthEnum.CATEGORY_ORDER.indexOf(a2.getCategory()));

		}).collect(Collectors.toList());

		return list;
	}

	@Transactional
	public String editAuth(Integer rowid, List<String> auths) {
		Users user = usersRepo.findById(rowid).orElse(null);

		if (user == null) {
			return "使用者不存在";
		}

		usersAuthRepo.deleteAll(user.getUsersAuths());
		user.getUsersAuths().clear();

		List<UsersAuth> newAuths = auths.stream().map(a -> {
			UsersAuth dao = new UsersAuth();
			dao.setUsers(user);
			dao.setAuthName(a);
			return dao;
		}).collect(Collectors.toList());

		user.setUsersAuths(newAuths);

		if (newAuths.size() > 0) {
			usersAuthRepo.saveAll(newAuths);
		}

		usersRepo.save(user);

		return null;
	}

	@Transactional
	public String chpwd(Integer rowid, String password) {

		Users user = usersRepo.findById(rowid).orElse(null);

		if (user == null) {
			return "使用者不存在";
		}

		String hashed = OneWayHash.SHA512(password);

		user.setPassword(hashed);

		usersRepo.save(user);

		return null;
	}

	public String delUser(Integer rowid) {

		Users user = usersRepo.findById(rowid).orElse(null);

		if (user == null) {
			return "使用者不存在";
		}

		usersRepo.delete(user);

		return null;
	}

}
