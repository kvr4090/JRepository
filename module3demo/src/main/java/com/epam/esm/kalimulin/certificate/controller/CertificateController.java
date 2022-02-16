	package com.epam.esm.kalimulin.certificate.controller;

import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.ENTITY_NOT_FOUND;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.controller.exception.ControllerException;
import com.epam.esm.kalimulin.certificate.controller.util.HateoasLinkBuilder;
import com.epam.esm.kalimulin.certificate.service.CertificateService;
import com.epam.esm.kalimulin.certificate.service.exception.EntityNotFoundException;
import com.epam.esm.kalimulin.certificate.service.exception.ServiceException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
	
	private final CertificateService certificateService;
	
	@Autowired
	public CertificateController(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

	@GetMapping
	public HttpEntity<List<Certificate>> getCertificate(
			@RequestParam(required = false) List<Long> tagsId,
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		PageModel model = new PageModel(page, size);
		try {
			List<Certificate> result = certificateService.findCertificate(tagsId, model);
			result = HateoasLinkBuilder.addLinksListCertificate(result, model, locale);
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		}
	}
	
	@GetMapping("/{id}")
	public HttpEntity<Certificate> getCertificate(
			@PathVariable int id,
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {	
		Certificate certificate = new Certificate(id);
		try {
			certificate = certificateService.findCertificateById(certificate);
			certificate = HateoasLinkBuilder.addLinks(certificate, new PageModel(page, size), locale);
			return new ResponseEntity<>(certificate, HttpStatus.OK);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		} catch (EntityNotFoundException e) {
			throw new ControllerException(locale, NOT_FOUND, ENTITY_NOT_FOUND, id);
		}
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@PostMapping
	public Certificate addCertificate(
			@RequestBody Certificate certificate,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		try {
			return certificateService.addCertificate(certificate);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST,exception.getMessage(), exception);
		}	
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@PutMapping
	public HttpEntity<Certificate> updateCertificate(
			@RequestBody Certificate certificate,
			@RequestParam(required = true, defaultValue = "0") Integer page,
			@RequestParam(required = true, defaultValue = "${default.page.size}") Integer size,
			@RequestParam(required = true, defaultValue = "${default.language}") String locale) 
					throws ControllerException {
		try {
			certificate = certificateService.updateCertificate(certificate);
			certificate = HateoasLinkBuilder.addLinks(certificate, new PageModel(page, size), locale);
			return new ResponseEntity<>(certificate, HttpStatus.CREATED); 
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST, exception.getMessage(), exception);
		} 
	}
	
	@PreAuthorize("hasAnyRole('ADMINISTRATOR')")
	@DeleteMapping("/{id}")
	public String deleteCertificate(
			@PathVariable int id, 
			@RequestParam(required = true, defaultValue = "${default.language}") String locale)
					throws ControllerException {	
		try {
			Certificate certificateForDelete = new Certificate();
			certificateForDelete.setId(id);
			return certificateService.deleteCertificate(certificateForDelete);
		} catch (ServiceException exception) {
			throw new ControllerException(locale, INTERNAL_SERVER_ERROR, exception);
		} catch (ValidationException exception) {
			throw new ControllerException(locale, BAD_REQUEST,exception.getMessage(), exception);
		} catch (EntityNotFoundException e) {
			throw new ControllerException(locale, NOT_FOUND, ENTITY_NOT_FOUND, id);
		}
	}
}