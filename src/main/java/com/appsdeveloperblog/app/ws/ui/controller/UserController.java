package com.appsdeveloperblog.app.ws.ui.controller;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appsdeveloperblog.app.ws.exceptions.UserServiceException;
import com.appsdeveloperblog.app.ws.service.AddressService;
import com.appsdeveloperblog.app.ws.service.UserService;
import com.appsdeveloperblog.app.ws.shared.dto.AddressDto;
import com.appsdeveloperblog.app.ws.shared.dto.UserDto;
import com.appsdeveloperblog.app.ws.ui.model.request.UserDetailsRequestModel;
import com.appsdeveloperblog.app.ws.ui.model.response.AddressesRest;
import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessages;
import com.appsdeveloperblog.app.ws.ui.model.response.OperationStatusModel;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationName;
import com.appsdeveloperblog.app.ws.ui.model.response.RequestOperationStatus;
import com.appsdeveloperblog.app.ws.ui.model.response.UserRest;

@RestController
@RequestMapping("/users") // http://localhost:8080/users
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	AddressService addressService;
	
	@Autowired
	AddressService addressesService;

	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUsers(@PathVariable String id) {
		UserRest returnval = new UserRest();
		returnval.setAddresses(new ArrayList<AddressesRest>());
		UserDto userDto = userService.getUserByUserId(id);

		System.out.println(userDto);
		// BeanUtils.copyProperties(userDto, returnval);

		ModelMapper modelMapper = new ModelMapper();
		returnval = modelMapper.map(userDto, UserRest.class);

		return returnval;
	}

	@PostMapping(consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public UserRest getUsersPost(@RequestBody UserDetailsRequestModel userDetailsRequestModel) throws Exception {
		// System.out.println(userDetailsRequestModel);
		// return "post user details was called";

		// return null;

		UserRest returnVal = new UserRest();

		if (userDetailsRequestModel.getFirstName().isEmpty())
			throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		if (userDetailsRequestModel.getLastName().isEmpty())
			throw new NullPointerException("the object is null");
		// UserDto userDto = new UserDto();
		// BeanUtils.copyProperties(userDetailsRequestModel, userDto);
		// model mapper is a better way of mapping objects which contain other objects

		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(userDetailsRequestModel, UserDto.class);

		UserDto createdUser = userService.createUser(userDto);

		// BeanUtils.copyProperties(createdUser, returnVal);

		returnVal = modelMapper.map(createdUser, UserRest.class);
		return returnVal;
	}

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })

	public UserRest getUsersPut(@PathVariable String id, @RequestBody UserDetailsRequestModel userDetails) {

		UserRest returnValue = new UserRest();
		UserDto userDto = new UserDto();
		BeanUtils.copyProperties(userDetails, userDto);
		UserDto updatedUser = userService.updateUser(id, userDto);
		BeanUtils.copyProperties(updatedUser, returnValue);

		return returnValue;
	}

	@DeleteMapping(path = "/{id}", consumes = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE })
	public OperationStatusModel getUsersDelete(@PathVariable String id) {

		OperationStatusModel returnVal = new OperationStatusModel();

		returnVal.setOperationName(RequestOperationName.DELETE.name());

		userService.deleteUser(id);

		returnVal.setOperationResult(RequestOperationStatus.SUCCESS.name());

		return returnVal;

	}

	@GetMapping(produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<UserRest> getUsers(@RequestParam(value = "page", defaultValue = "2") int page,
			@RequestParam(value = "limit", defaultValue = "2") int limit) {
		List<UserRest> returnval = new ArrayList<>();

		List<UserDto> users = userService.getUsers(page, limit);

		for (UserDto userDto : users) {
			UserRest userModel = new UserRest();
			BeanUtils.copyProperties(userDto, userModel);
			returnval.add(userModel);
		}

		return returnval;
	}

	// http://localhost:8080/mobile-app-ws/users/asdadadas/addresses
	@GetMapping(path = "/{id}/addresses", produces = { MediaType.APPLICATION_XML_VALUE,
			MediaType.APPLICATION_JSON_VALUE })
	public List<AddressesRest> getUserAddresses(@PathVariable String id) {

		List<AddressesRest> returnval = new ArrayList<>();

		List<AddressDto> addressesDTO = addressesService.getAddresses(id);

		java.lang.reflect.Type listType = new TypeToken<List<AddressesRest>>() {
		}.getType();
		ModelMapper modelMapper = new ModelMapper();
		returnval = modelMapper.map(addressesDTO, listType);

		return returnval;

		// UserRest returnval = new UserRest();
		// returnval.setAddresses(new ArrayList<AddressesRest>());
		// UserDto userDto = userService.getUserByUserId(id);

		// System.out.println(userDto);
		// BeanUtils.copyProperties(userDto, returnval);

		// ModelMapper modelMapper = new ModelMapper();
		// returnval=modelMapper.map(userDto, UserRest.class);

		// return returnval;
	}

	@GetMapping(path = "/{userId}/addresses/{addressId}", produces = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	public AddressesRest getUserAddress(@PathVariable String addressId) {
		AddressDto addressesDto = addressService.getAddress(addressId);
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(addressesDto, AddressesRest.class);
	}

}
