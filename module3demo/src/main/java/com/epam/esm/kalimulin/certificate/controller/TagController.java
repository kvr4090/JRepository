package com.epam.esm.kalimulin.certificate.controller;

import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.ENTITY_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Tag;
import com.epam.esm.kalimulin.certificate.controller.exception.ControllerException;
import com.epam.esm.kalimulin.certificate.controller.util.HateoasLinkBuilder;
import com.epam.esm.kalimulin.certificate.service.TagService;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

@RestController
@RequestMapping("/tags")
public class TagController {
	
	private final TagService tagService;

	@Autowired
	public TagController(TagService tagService) {
		this.tagService = tagService;
	}
	
	@GetMapping("/mptfru")
	public HttpEntity<Tag> getMostPopularTagFromRichestUser(
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		try {
			Tag result = tagService.findMostPopularTagFromRichestUser();
			result = HateoasLinkBuilder.addLinks(result, new PageModel(page, size), locale);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
	}
	
	@GetMapping
	public HttpEntity<List<Tag>> getAllTags(
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		PageModel model = new PageModel(page, size);
		try {
			List<Tag> result = tagService.findAllTags(model);
			result = HateoasLinkBuilder.addLinksListTag(result, model, locale);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
	}

	@GetMapping("/{id}")
	public HttpEntity<Tag> getTag(
			@PathVariable int id,
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException, ValidationException {
		Tag requiredTag = new Tag(id);
		try {
			requiredTag = tagService.findTag(requiredTag);
			requiredTag = HateoasLinkBuilder.addLinks(requiredTag, new PageModel(page, size), locale);
			return new ResponseEntity<>(requiredTag, HttpStatus.OK);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (EntityNotFoundException e) {
			throw new ControllerException(locale, NOT_FOUND, ENTITY_NOT_FOUND, id);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@PostMapping
	public ResponseEntity<Tag> addTag(
			@RequestBody Tag tag,
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		try {
			tag = tagService.addTag(tag);
			tag = HateoasLinkBuilder.addLinks(tag, new PageModel(page, size), locale);
			return new ResponseEntity<>(tag, CREATED);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@DeleteMapping("/{id}")
	public String deleteTag(
			@PathVariable int id, 
			@RequestParam(required = true, defaultValue = "${default.language}") String locale)
					throws ControllerException {
		try {
			 return tagService.deleteTag(new Tag(id));
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
	}
}
