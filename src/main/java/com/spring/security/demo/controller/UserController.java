package com.spring.security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.spring.security.demo.service.UserService;

/**
 * @author surajs
 *
 */
@RestController
@RequestMapping(value = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

	@Autowired
	private UserService userService;

	/* Get all users */
	@RequestMapping(value = "/user/getAll", method = RequestMethod.GET)
	@PreAuthorize("hasPermission('hasAccess', 'VIEW')")
	public ResponseEntity<Object> getAll() {
		return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
	}

	/* Get Assign */
	@RequestMapping(value = "/user/assign/{id}", method = RequestMethod.POST)
	@PreAuthorize("hasPermission('hasAccess', 'ASSIGN')")
	public ResponseEntity<String> assign(@PathVariable Long id) {
		return new ResponseEntity<>("{\"message\":\"Assignment is successfully\",\"code\":" + id + "}", HttpStatus.OK);
	}

	/* View */
	@RequestMapping(value = "/user/get/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasPermission('hasAccess', 'VIEW')")
	public ResponseEntity<String> get(@PathVariable String string) {
		return new ResponseEntity<>("{\"message\":\"Success\",\"code\":600000}" + string, HttpStatus.OK);
	}

	/* update user */
	@RequestMapping(value = "/user/update/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasPermission('hasAccess', 'UPDATE')")
	public ResponseEntity<String> update(@PathVariable Long id) {
		return new ResponseEntity<>("{\"message\":\"Updated successfully\",\"code\":600000}", HttpStatus.OK);
	}

	/* Get delete single user */
	@RequestMapping(value = "/user/delete/{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasPermission('hasAccess', 'DELETE')")
	public ResponseEntity<String> delete(@PathVariable Long id) {
		return new ResponseEntity<>("{\"message\":\"Deleted successfully\",\"code\":600000}", HttpStatus.OK);
	}

	/* Revoke user access */
	@RequestMapping(value = "/user/revoke/{id}", method = RequestMethod.GET)
	@PreAuthorize("hasPermission('hasAccess', 'REVOKE')")
	public ResponseEntity<String> invalidateUser(@PathVariable Long id) {
		return new ResponseEntity<>("{\"message\":\"User accesses are revoked successfully\",\"code\":600000}",
				HttpStatus.OK);
	}

	/* Create a user */
	@RequestMapping(value = "/user/create", method = RequestMethod.POST)
	@PreAuthorize("hasPermission('hasAccess', 'CREATE')")
	public ResponseEntity<Object> createUser(@RequestBody Object object) {
		return new ResponseEntity<>(object, HttpStatus.CREATED);
	}
}