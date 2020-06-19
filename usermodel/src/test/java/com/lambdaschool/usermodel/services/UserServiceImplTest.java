package com.lambdaschool.usermodel.services;

import com.lambdaschool.usermodel.UserModelApplication;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserModelApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void afindUserById() {
        assertEquals("misskitty test", userService.findUserById(14).getUsername());
    }

    @Test(expected = EntityNotFoundException.class)
    public void aafindUserByIdFail() {
        assertEquals("misskitty test", userService.findUserById(99999).getUsername());
    }

    @Test
    public void bfindByNameContaining() {
        assertEquals("barnbarn test", userService.findByNameContaining("barn").get(0).getUsername());
        //System.out.println(userService.findByNameContaining("kitty").get(0).getUserid());
    }

    @Test
    public void cfindAll() {

        assertEquals(30, userService.findAll().size());

    }

    @Test
    public void ddelete() {
        userService.delete(14);
        assertEquals(29, userService.findAll().size());
    }

    @Test(expected = EntityNotFoundException.class)
    public void dadelete() {
        userService.delete(99999);
        assertEquals(29, userService.findAll().size());
    }

    @Test
    public void efindByName() {
        assertEquals("barnbarn test", userService.findByName("barnbarn test").getUsername());
    }

    @Test
    public void eagetCountUserEmails() {
        ArrayList<UserRoles> admins = new ArrayList<>();
        assertEquals(28, userService.getCountUserEmails().size());
    }

    @Test
    public void fsave() {
        ArrayList<UserRoles> admins = new ArrayList<>();

        User u1 = new User("first TEST",
                "password",
                "first@lambdaschool.local",
                admins);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        User newUser = userService.save(u1);
        assertNotNull(newUser);
        User foundUser = userService.findUserById(newUser.getUserid());
        assertEquals(newUser.getUsername(), foundUser.getUsername());

    }

    @Test
    public void gupdate() {
        ArrayList<UserRoles> admins = new ArrayList<>();
        Role role = roleService.findByName("admin");
        admins.add(new UserRoles(new User(),
                role));

        User u1 = new User("second TEST",
                "password",
                "fun@times.com",
                admins);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        User newUser = userService.save(u1);
        assertEquals("fun@times.com", newUser.getPrimaryemail());
    }

    @Test(expected = EntityNotFoundException.class)
    public void HA_deleteUserRoleRoleNotFound()
    {
        userService.deleteUserRole(7, 50);
    }

    @Test(expected = EntityNotFoundException.class)
    public void HB_deleteUserRoleUserNotFound()
    {
        userService.deleteUserRole(50, 2);
    }

    @Test(expected = EntityNotFoundException.class)
    public void IC_addUserRoleRoleNotFound()
    {
        userService.addUserRole(7, 50);
    }

    @Test(expected = EntityNotFoundException.class)
    public void ID_addUserRoleUserNotFound()
    {
        userService.addUserRole(50, 2);
    }
}