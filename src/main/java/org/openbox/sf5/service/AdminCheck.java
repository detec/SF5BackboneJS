package org.openbox.sf5.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.openbox.sf5.dao.DAO;
import org.openbox.sf5.model.Users;
import org.openbox.sf5.model.Usersauthorities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Creates admin user on context load.
 *
 * @author Andrii Duplyk
 *
 */
@Component // NFO: Overriding bean definition for bean
public class AdminCheck {

	@Autowired
	private DAO objectsController;

	private boolean adminHasBeenChanged = false;

	/**
	 *
	 * @param event
	 */
	@EventListener
	public void handleContextRefresh(ContextRefreshedEvent event) {

		Criterion criterea = Restrictions.eq("username", "admin");
		List<Users> adminsList = objectsController.findAllWithRestrictions(Users.class, criterea);

		if (adminsList.isEmpty()) {
			List<Usersauthorities> rolesList = new ArrayList<>();

			Users admin = new Users("admin", "1", true, rolesList);

			fillTables(admin, rolesList);
			objectsController.saveOrUpdate(admin);
		}

		else {

			Users adminUser = adminsList.get(0);
			List<Usersauthorities> rolesList = adminUser.getAuthorities();

			fillTables(adminUser, rolesList);

			if (adminHasBeenChanged) {
				objectsController.saveOrUpdate(adminUser);
			}

		}

	}

	private void fillTables(Users adminUser, List<Usersauthorities> rolesList) {
		List<String> textRoles = new ArrayList<>();
		textRoles.add("ROLE_ADMIN");
		textRoles.add("ROLE_USER");

		// ROLE_ADMIN
		Usersauthorities checkRoleAdmin = new Usersauthorities(adminUser.getUsername(), "ROLE_ADMIN", adminUser, 1);

		if (!rolesList.contains(checkRoleAdmin)) {
			adminHasBeenChanged = true;
			rolesList.add(checkRoleAdmin);
		}

		// ROLE_USER
		Usersauthorities checkRoleUser = new Usersauthorities(adminUser.getUsername(), "ROLE_USER", adminUser, 2);

		if (!rolesList.contains(checkRoleUser)) {
			adminHasBeenChanged = true;
			rolesList.add(checkRoleUser);
		}

	}

}
