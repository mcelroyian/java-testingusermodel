package com.lambdaschool.usermodel.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.usermodel.models.Role;
import com.lambdaschool.usermodel.models.User;
import com.lambdaschool.usermodel.models.UserRoles;
import com.lambdaschool.usermodel.models.Useremail;
import com.lambdaschool.usermodel.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private List<User> userList;

    @Before
    public void setUp() throws Exception {

        userList = new ArrayList<>();

        Role r1 = new Role("admin");
        r1.setRoleid(1);
        Role r2 = new Role("user");
        r2.setRoleid(2);
        Role r3 = new Role("data");
        r3.setRoleid(3);


        // admin, data, user
        ArrayList<UserRoles> admins = new ArrayList<>();

        admins.add(new UserRoles(new User(),
                r1));
        admins.add(new UserRoles(new User(),
                r2));
        admins.add(new UserRoles(new User(),
                r3));


        User u1 = new User("admin TEST",
                "password",
                "admin@lambdaschool.local",
                admins);
        u1.setUserid(11);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@email.local"));
        u1.getUseremails().get(0).setUseremailid(111);
        u1.getUseremails()
                .add(new Useremail(u1,
                        "admin@mymail.local"));
        u1.getUseremails().get(1).setUseremailid(112);

        userList.add(u1);

        // data, user
        ArrayList<UserRoles> datas = new ArrayList<>();
        datas.add(new UserRoles(new User(),
                r3));
        datas.add(new UserRoles(new User(),
                r2));

        User u2 = new User("cinnamon TEST",
                "1234567",
                "cinnamon@lambdaschool.local",
                datas);
        u2.setUserid(21);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "cinnamon@mymail.local"));
        u2.getUseremails().get(0).setUseremailid(211);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "hops@mymail.local"));
        u2.getUseremails().get(1).setUseremailid(212);
        u2.getUseremails()
                .add(new Useremail(u2,
                        "bunny@email.local"));
        u2.getUseremails().get(2).setUseremailid(213);

        userList.add(u2);

        // user
        ArrayList<UserRoles> users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u3 = new User("barnbarn TEST",
                "ILuvM4th!",
                "barnbarn@lambdaschool.local",
                users);

        u3.setUserid(31);
        u3.getUseremails()
                .add(new Useremail(u3,
                        "barnbarn@email.local"));
        u3.getUseremails().get(0).setUseremailid(311);
        userList.add(u3);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u4 = new User("puttat TEST",
                "password",
                "puttat@school.lambda",
                users);
        u4.setUserid(41);
        userList.add(u4);

        users = new ArrayList<>();
        users.add(new UserRoles(new User(),
                r2));
        User u5 = new User("misskitty TEST",
                "password",
                "misskitty@school.lambda",
                users);
        u5.setUserid(51);

        userList.add(u5);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllUsers() throws Exception {
        String apiUrl = "/users/users";
        Mockito.when(userService.findAll()).thenReturn(userList);
        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl).accept(MediaType.APPLICATION_JSON);
        MvcResult results = mockMvc.perform(rb).andReturn();
        String testResults = results.getResponse().getContentAsString();
        ObjectMapper mapper = new ObjectMapper();

        String expectedResults = mapper.writeValueAsString(userList);
        assertEquals(expectedResults, testResults);
    }

    @Test
    public void getUserById() throws Exception {
        String apiUrl = "/users/user/31";
        Mockito.when(userService.findUserById(31))
            .thenReturn(userList.get(1));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn(); // this could throw an exception
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(1));

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void getUserByName() throws Exception {
        String apiUrl = "/users/user/name/admin TEST";

        Mockito.when(userService.findByName("admin TEST"))
                .thenReturn(userList.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb)
                .andReturn(); // this could throw an exception
        String tr = r.getResponse()
                .getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(userList.get(0));

        System.out.println("Expect: " + er);
        System.out.println("Actual: " + tr);

        assertEquals("Rest API Returns List", er, tr);
    }

    @Test
    public void getUserLikeName() {
    }

    @Test
    public void addNewUser() throws Exception {

        String apiUrl = "/users/user";

        // build a restaurant
        ArrayList<UserRoles> thisRoles = new ArrayList<>();
        String newUserName = "Number 1 Test Eats";
        User r3 = new User(newUserName,
                "565 Side Test Avenue",
                "email@Email.com",
                thisRoles);
        r3.setUserid(100);
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(r3);

        Mockito.when(userService.save(any(User.class)))
                .thenReturn(r3);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb)
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser() throws Exception {

        String apiUrl = "/users/user/200";

        // build a restaurant
        ArrayList<UserRoles> thisRoles = new ArrayList<>();
        String newUserName = "Number 2 Test Eats";
        User r3 = new User(newUserName,
                "565 Side Test Avenue",
                "email@Email.com",
                thisRoles);
        r3.setUserid(200);
        Mockito.when(userService.update(r3, 200L))
                .thenReturn(r3);
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(r3);

        Mockito.when(userService.save(any(User.class)))
                .thenReturn(r3);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, 200L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUser() throws Exception{
        String apiUrl = "/users/user/300";

        // build a restaurant
        ArrayList<UserRoles> thisRoles = new ArrayList<>();
        String newUserName = "Number 3 Test Eats";
        User r3 = new User(newUserName,
                "565 Side Test Avenue",
                "email@Email.com",
                thisRoles);
        r3.setUserid(300);
        Mockito.when(userService.update(r3, 300L))
                .thenReturn(r3);
        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(r3);

        Mockito.when(userService.save(any(User.class)))
                .thenReturn(r3);

        RequestBuilder rb = MockMvcRequestBuilders.patch(apiUrl, 300L)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(userString);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserById() throws Exception {
        String apiUrl = "/users/user/{restaurantid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "200")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void getNumUserEmails() throws Exception {
        String apiUrl = "/users/user/email/count";

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserRoleByIds() throws Exception{
        String apiUrl = "/users/user/{id}";
        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, 11)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb).andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void postUserRoleByIds() throws Exception {
       String apiUrl = "/users/user/{userid}/role/{roleid}";
        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl, 300, 11)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb).andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
    }
}