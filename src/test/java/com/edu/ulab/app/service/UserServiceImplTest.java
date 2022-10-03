package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link com.edu.ulab.app.service.impl.UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {
        //given

        UserDto userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        Person person = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");


        //when

        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);


        //then

        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {
        //given
        UserDto userUpdateDto = new UserDto();
        userUpdateDto.setId(1L);
        userUpdateDto.setAge(11);
        userUpdateDto.setFullName("new name");
        userUpdateDto.setTitle("test title");
        userUpdateDto.setGender("woman");

        Person updatePerson = new Person();
        updatePerson.setId(1L);
        updatePerson.setAge(11);
        updatePerson.setFullName("new name");
        updatePerson.setTitle("test title");
        updatePerson.setGender("woman");

        UserDto updateResult = new UserDto();
        updateResult.setId(1L);
        updateResult.setAge(11);
        updateResult.setFullName("new name");
        updateResult.setTitle("test title");
        updateResult.setGender("woman");

        //when
        when(userMapper.userDtoToPerson(userUpdateDto)).thenReturn(updatePerson);
        when(userRepository.findById(updatePerson.getId())).thenReturn(Optional.of(updatePerson));
        when(userRepository.save(updatePerson)).thenReturn(updatePerson);
        when(userMapper.personToUserDto(updatePerson)).thenReturn(updateResult);

        //then
        UserDto result = userService.updateUser(userUpdateDto);
        assertEquals("new name", result.getFullName());
    }

    @Test
    @DisplayName("Получение пользователя. Должно пройти успешно.")
    void getPerson_Test() {
        //given
        Long personId = 1L;

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setAge(11);
        savedPerson.setFullName("old name");
        savedPerson.setTitle("test title");
        savedPerson.setGender("woman");

        UserDto result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("old name");
        result.setTitle("test title");
        result.setGender("woman");

        //when
        when(userRepository.findById(personId)).thenReturn(Optional.of(savedPerson));
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //then
        UserDto getUserDto = userService.getUserById(savedPerson.getId());

        assertEquals(1L, getUserDto.getId());
        assertEquals(11L, getUserDto.getAge());
        assertEquals("old name", getUserDto.getFullName());
        assertEquals("test title", getUserDto.getTitle());
        assertEquals("woman", getUserDto.getGender());
    }


    @Test
    @DisplayName("Удаление пользователя. Должно пройти успешно.")
    void deletePerson_Test() {
        //given
        Long personId = 1L;

        //when
        doNothing().when(userRepository).deleteById(personId);

        //then
        userService.deleteUserById(personId);
    }

    // * failed
    @Test
    @DisplayName("Ошибка при выдаче пользователя. Должно пройти успешно.")
    void getFailedPerson_Test() {
        //given
        Long userId = 1L;

        Person savedPerson = new Person();
        savedPerson.setId(userId);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");
        savedPerson.setGender("man");

        UserDto result = new UserDto();
        result.setId(userId);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");
        result.setGender("man");

        //when
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //then
        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found!");
    }

}
