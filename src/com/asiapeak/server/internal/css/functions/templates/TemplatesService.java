package com.asiapeak.server.internal.css.functions.templates;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.asiapeak.server.internal.css.dao.entity.Templates;
import com.asiapeak.server.internal.css.dao.repo.TemplatesRepo;
import com.asiapeak.server.internal.css.functions.templates.dto.TemplatesInputDto;
import com.asiapeak.server.internal.css.functions.templates.dto.TemplatesOutputDto;
import com.asiapeak.server.internal.css.system.UserNameService;

@Service
public class TemplatesService {

	@Autowired
	TemplatesRepo templatesRepo;

	@Autowired
	UserNameService userNameService;

	@Transactional
	public List<TemplatesOutputDto> list() {
		return templatesRepo.findAll().stream().map(dao -> {
			TemplatesOutputDto dto = new TemplatesOutputDto();
			dto.setRowid(dao.getRowid());
			dto.setName(dao.getName());
			dto.setInfoColumns(dao.getInfoColumns());
			return dto;
		}).collect(Collectors.toList());
	}

	@Transactional
	public String create(TemplatesInputDto dto) {

		Templates dao = new Templates();

		dao.setName(dto.getName());
		dao.setInfoColumns(dto.getInfoColumns());

		String user = userNameService.getCurrentUserName();
		Date now = new Date();

		dao.setUdate(now);
		dao.setUuser(user);
		dao.setCdate(now);
		dao.setCuser(user);
		templatesRepo.save(dao);

		return null;
	}

	@Transactional
	public String update(Integer rowid, TemplatesInputDto dto) {
		Templates dao = templatesRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return "範本不存在";
		}

		dao.setName(dto.getName());
		dao.setInfoColumns(dto.getInfoColumns());

		String user = userNameService.getCurrentUserName();
		Date now = new Date();

		dao.setUdate(now);
		dao.setUuser(user);
		templatesRepo.save(dao);
		return null;
	}

	@Transactional
	public String delete(Integer rowid) {
		Templates dao = templatesRepo.findById(rowid).orElse(null);
		if (dao == null) {
			return "範本不存在";
		}
		templatesRepo.delete(dao);
		return null;
	}

	public TemplatesOutputDto getTemplate(Integer rowid) {
		Templates dao = templatesRepo.findById(rowid).orElse(null);
		TemplatesOutputDto dto = new TemplatesOutputDto();
		dto.setRowid(dao.getRowid());
		dto.setName(dao.getName());
		dto.setInfoColumns(dao.getInfoColumns());
		return dto;
	}

}
