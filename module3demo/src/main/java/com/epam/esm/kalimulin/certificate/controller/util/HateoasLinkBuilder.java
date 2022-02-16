package com.epam.esm.kalimulin.certificate.controller.util;

import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.CERTIFICATES;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.FIRST_PAGE;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.LAST;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.MPTFRU;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.NEXT_PAGE;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.ORDERS;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.PREVIOUS_PAGE;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.SELF;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.TAGS;
import static com.epam.esm.kalimulin.certificate.controller.util.ControllerConstant.USERS;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import com.epam.esm.kalimulin.certificate.bean.Certificate;
import com.epam.esm.kalimulin.certificate.bean.Order;
import com.epam.esm.kalimulin.certificate.bean.PageModel;
import com.epam.esm.kalimulin.certificate.bean.Tag;
import com.epam.esm.kalimulin.certificate.bean.user.UserClient;
import com.epam.esm.kalimulin.certificate.controller.CertificateController;
import com.epam.esm.kalimulin.certificate.controller.OrderController;
import com.epam.esm.kalimulin.certificate.controller.TagController;
import com.epam.esm.kalimulin.certificate.controller.UserController;
import com.epam.esm.kalimulin.certificate.controller.exception.ControllerException;
import com.epam.esm.kalimulin.certificate.service.exception.ValidationException;

public class HateoasLinkBuilder {
	
	public static UserClient addLinks(UserClient user, PageModel model, String locale) throws ControllerException {
		int page = model.getPage();
		int size = model.getSize();
		user.add(linkTo(methodOn(UserController.class).getUser((int) user.getId(), page, size, locale)).withSelfRel());
		user.add(linkTo(methodOn(OrderController.class).getOrder(user.getId(), page, size, locale)).withRel(ORDERS));
		user.add(linkTo(methodOn(TagController.class).getMostPopularTagFromRichestUser(0, 1, locale)).withRel(MPTFRU));
		user.add(linkTo(methodOn(TagController.class).getAllTags(page, size, locale)).withRel(TAGS));
		user.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page, size, locale))
				.withRel(CERTIFICATES));		
		return user;
	}
	
	public static List<UserClient> addLinksListUser(List<UserClient> list, PageModel model, String locale)
			throws ControllerException {
		int page = model.getPage();
		int size = model.getSize();
		for (UserClient user : list) {
			user = addLinks(user, model, locale);
			user.add(linkTo(methodOn(UserController.class).getUsers(0, size, locale)).withRel(FIRST_PAGE));
			user.add(linkTo(methodOn(UserController.class).getUsers(page + size, size, locale)).withRel(NEXT_PAGE));
			user.add(linkTo(methodOn(UserController.class).getUsers(page - size, size, locale))
					.withRel(PREVIOUS_PAGE));
			user.add(linkTo(methodOn(UserController.class).getUsers(page, size, locale)).withRel(SELF));
		}
		return list;
	}
	
	public static Order addLinks(Order order, PageModel model, String locale) throws ControllerException {
		int page = model.getPage();
		int size = model.getSize();
		order.add(linkTo(methodOn(OrderController.class).getOrder(order.getUser().getId(), page, size, locale))
				.withSelfRel());
		order.add(linkTo(methodOn(TagController.class).getMostPopularTagFromRichestUser(0, 1, locale))
				.withRel(MPTFRU));
		order.add(linkTo(methodOn(TagController.class).getAllTags(page, size, locale)).withRel(TAGS));
		order.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page, size, locale))
				.withRel(CERTIFICATES));
		order.add(linkTo(methodOn(UserController.class).getUsers(0, size, locale)).withRel(USERS));
		return order;
	}
	
	public static List<Order> addLinksListOrder(List<Order> list, PageModel model, String locale)
			throws ControllerException {
		for (Order order : list) {
			int page = model.getPage();
			int size = model.getSize();
			order = addLinks(order, model, locale);
			order.add(linkTo(methodOn(OrderController.class).getOrder(order.getUser().getId(), page, size, locale))
					.withSelfRel());
			order.add(linkTo(methodOn(OrderController.class).getOrder((long) 0, page, size, locale))
					.withRel(FIRST_PAGE));
			order.add(linkTo(methodOn(OrderController.class).getOrder((long) 0, page + size, size, locale))
					.withRel(NEXT_PAGE));
			order.add(linkTo(methodOn(OrderController.class).getOrder((long) 0, page - size, size, locale))
					.withRel(PREVIOUS_PAGE));
			order.add(linkTo(methodOn(OrderController.class).getOrder((long) 0, page, size, locale))
					.withRel(SELF));
			order.add(linkTo(methodOn(OrderController.class).getOrder((long) 0, 
					(int) Math.ceil(list.size()/model.getSize()), size, locale)).withRel(LAST));
		}
		return list;	
	}
	
	public static Tag addLinks(Tag tag, PageModel model, String locale)
			throws ControllerException, ValidationException {
		int page = model.getPage();
		int size = model.getSize();
		tag.add(linkTo(methodOn(TagController.class).getTag((int) tag.getId(), page, size, locale)).withSelfRel());
		tag.add(linkTo(methodOn(TagController.class).getAllTags(page, size, locale)).withRel(TAGS));
		tag.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page, size,locale))
				.withRel(CERTIFICATES));
		tag.add(linkTo(methodOn(OrderController.class).getOrder(null, page, size, locale)).withRel(ORDERS));
		tag.add(linkTo(methodOn(TagController.class).getMostPopularTagFromRichestUser(0, 20, locale)).withRel(MPTFRU));
		return tag;
	}
	
	public static List<Tag> addLinksListTag(List<Tag> list, PageModel model, String locale) 
			throws ControllerException, ValidationException {
		for (Tag tag : list) {
			int page = model.getPage();
			int size = model.getSize();
			tag = addLinks(tag, model, locale);
			tag.add(linkTo(methodOn(TagController.class).getAllTags(page, size, locale)).withRel(FIRST_PAGE));
			tag.add(linkTo(methodOn(TagController.class).getAllTags(page++, size, locale)).withRel(NEXT_PAGE));
			tag.add(linkTo(methodOn(TagController.class).getAllTags(page--, size, locale)).withRel(PREVIOUS_PAGE));
			tag.add(linkTo(methodOn(TagController.class).getAllTags(page, size, locale)).withRel(SELF));
		}
		return list;
	}
	
	public static Certificate addLinks(Certificate certificate, PageModel model, String locale)
			throws ControllerException {
		int page = model.getPage();
		int size = model.getSize();
		certificate.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page, size, locale))
				.withSelfRel());
		certificate.add(linkTo(methodOn(TagController.class).getAllTags(page, size, locale)).withRel(TAGS));
		certificate.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page, size,locale))
				.withRel(CERTIFICATES));
		certificate.add(linkTo(methodOn(OrderController.class).getOrder(null, page, size, locale)).withRel(ORDERS));
		certificate.add(linkTo(methodOn(TagController.class).getMostPopularTagFromRichestUser(0, 1, locale))
				.withRel(MPTFRU));
		return certificate;
	}
	
	public static List<Certificate> addLinksListCertificate(List<Certificate> list, PageModel model, String locale)
			throws ControllerException {
		int page = model.getPage();
		int size = model.getSize();
		for (Certificate certificate : list) {
			certificate = addLinks(certificate, model, locale);
			certificate.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page, size, locale))
					.withSelfRel());
			certificate.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page, size, locale))
					.withRel(FIRST_PAGE));
			certificate.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page + size, size,
					locale)).withRel(NEXT_PAGE));
			certificate.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page - size, size,
					locale)).withRel(PREVIOUS_PAGE));
			certificate.add(linkTo(methodOn(CertificateController.class).getCertificate(null, page, size, locale))
					.withRel(SELF));
		}
		return list;	
	}	
}
