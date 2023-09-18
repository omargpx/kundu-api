package com.citse.kunduApp.mock;

import com.citse.kunduApp.entity.Person;
import com.citse.kunduApp.entity.User;
import com.citse.kunduApp.exceptions.KunduException;
import com.citse.kunduApp.repository.PersonDao;
import com.citse.kunduApp.utils.contracts.PersonService;
import com.citse.kunduApp.utils.logic.PersonImp;
import com.citse.kunduApp.utils.models.Role;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PersonServiceTest {

    @InjectMocks
    private PersonImp personService;

    @Mock
    private PersonDao personRepository;

    @Test
    public void testSearchPersonWithResults() {
        // test config
        MockitoAnnotations.openMocks(this);
        String query = "Omar";
        List<Person> expectedResult = Collections.singletonList(getObject());

        // simulate repo behavior
        when(personRepository.searchByFullNameOrNickname(anyString())).thenReturn(expectedResult);

        // run function test
        List<Person> result = personService.searchPerson(query);

        // verify results
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSearchPersonWithNoResults() {
        // config
        MockitoAnnotations.openMocks(this);
        String query = "Luckovichk";

        when(personRepository.searchByFullNameOrNickname(anyString())).thenReturn(Collections.emptyList());
        assertEquals("Username not found", new Object());
    }

    private Person getObject(){
        return Person.builder()
                .id(25)
                .name("Omar gp")
                .phone("569654129")
                .avatar("io.lsd")
                .kunduCode("KSC23-ÑlVk5gSI1")
                .biography(null)
                .experience(0)
                .birth(null)
                .joinDate(LocalDate.parse("2023-09-13"))
                .userDetail(User.builder()
                        .id(25)
                        .username("omargpax")
                        .email("omarguerrero@gmail.com")
                        .role(Role.USER)
                        .lastConnect(LocalDateTime.parse("2023-09-13T14:21:53"))
                        .invitations(new ArrayList<>())
                        .guests(new ArrayList<>())
                        .build())
                .member(null)
                .build();
    }
}
