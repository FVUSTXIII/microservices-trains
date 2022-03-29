package com.trainservice.java.serviceimpl.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.trainservice.java.dto.TrainAllResponseDTO;
import com.trainservice.java.dto.TrainResponseDTO;
import com.trainservice.java.dto.TripRequestDTO;
import com.trainservice.java.dto.TripResponseDTO;
import com.trainservice.java.entity.Route;
import com.trainservice.java.entity.Train;
import com.trainservice.java.entity.Trips;
import com.trainservice.java.exception.RouteNotFoundException;
import com.trainservice.java.exception.TrainListEmptyException;
import com.trainservice.java.exception.TripsNotFoundException;
import com.trainservice.java.repository.RouteRepository;
import com.trainservice.java.repository.TrainRepository;
import com.trainservice.java.repository.TripRepository;
import com.trainservice.java.serviceimpl.TrainServiceImpl;

@ExtendWith(MockitoExtension.class)
public class TrainServiceImplTest {
	
	@Mock
	RouteRepository routeRepository;
	
	@Mock
	TrainRepository trainRepository;
	
	@Mock
	TripRepository tripRepository;
	
	@Mock
	Page<Train> page;
	
	@Mock
	List<Train> list1,list2;
	
	@Mock
	Train train1, train2, trainTarget, trainTarget2;
	
	@InjectMocks
	TrainServiceImpl trainServiceImpl;
	
	
	TripRequestDTO tripRequestDTO;
	TripRequestDTO tripRequestDTO2;
	TripRequestDTO tripRequestDTO3;
	TripRequestDTO tripRequestDTO4;
	
	TripResponseDTO tripResponseDTO;
	
	
	Page<Train> pagelist, pagelist2;
	
	
	Validator validator;
	
	Train train;
	Train train2A;
	Train train3;
	Optional<Train> trainOp1, trainTargetOp, trainTargetOp2;
	Optional<Train> trainOp2;
	Optional<Train> trainOp3;
	
	Route route;
	Optional<Route> route2;
	
	Trips trip;
	Trips trip2;
	Trips trip3;
	
	List<Trips> tripsList;
	List<Trips> emptyList;
	
	Page<Trips> pageList;
	Page<Trips> emptyPage;
	
	Pageable paging;
	
	
	@BeforeEach
	public void setUp() {		
	
		list1= new ArrayList<Train>();
		list2= new ArrayList<Train>();
		
		trainTarget= new Train();
		trainTarget.setCapacity(50);
		trainTarget.setTrainId(1);
		trainTarget.setTrainName("Train Target");
		trainTarget.setTrainScore(5.1);
		trainTarget.setTrainType(3.4);
		
		trainTargetOp = Optional.of(trainTarget);

		trainTarget2= new Train();
		trainTarget2.setCapacity(50);
		trainTarget2.setTrainId(1);
		trainTarget2.setTrainName("Train Target");
		trainTarget2.setTrainScore(5.1);
		trainTarget2.setTrainType(3.4);
		
		trainTargetOp2 = Optional.of(trainTarget2);
		
		train1= new Train();
		train1.setCapacity(50);
		train1.setTrainId(1);
		train1.setTrainName("Train 1");
		train1.setTrainScore(5.1);
		train1.setTrainType(3.4);
		
		train2= new Train();
		train2.setCapacity(30);
		train2.setTrainId(2);
		train2.setTrainName("Train 2");
		train2.setTrainScore(5.0);
		train2.setTrainType(4.6);
		
		list1.add(train1);
		list1.add(train2);
		
		paging = PageRequest.of(0,5);
		pagelist = new PageImpl<Train>(list1, paging, list1.size()); 
		pagelist2 = new PageImpl<Train>(list2, paging, list2.size()); 
		
		
		
		//--------------------------------------------------------
		
		//Positive case
				tripRequestDTO = new TripRequestDTO();
				tripRequestDTO.setSource("Ciudad Guzman");
				tripRequestDTO.setDestination("Guadalajara");
				tripRequestDTO.setDate(LocalDate.parse("2022-03-28"));
				
				//Negative Case (No route with given source and destination)
				tripRequestDTO2 = new TripRequestDTO();
				tripRequestDTO2.setSource("Manzanillo");
				tripRequestDTO2.setDestination("Ciudad Guzman");
				tripRequestDTO2.setDate(LocalDate.parse("2022-03-28"));
				
				//Negative Case (No trip with given date)
				tripRequestDTO3 = new TripRequestDTO();
				tripRequestDTO3.setSource("Ciudad Guzman");
				tripRequestDTO3.setDestination("Guadalajara");
				tripRequestDTO3.setDate(LocalDate.parse("2022-04-13"));
				
				//Validation Case (No data on the DTO)
				tripRequestDTO4 = new TripRequestDTO();
				tripRequestDTO4.setSource("");
				tripRequestDTO4.setDestination("");
				tripRequestDTO4.setDate(LocalDate.parse("2022-04-13"));
				
				train = new Train();
				train.setTrainId(1);
				train.setCapacity(500);
				train.setTrainName("El Tacoriendo");
				train.setTrainScore(4.3);
				train.setTrainType(1.2);
				
				train2A = new Train();
				train2A.setTrainId(2);
				train2A.setCapacity(300);
				train2A.setTrainName("El Mostacho");
				train2A.setTrainScore(4.8);
				train2A.setTrainType(2.0);
				
				train3 = new Train();
				train3.setTrainId(3);
				train3.setCapacity(400);
				train3.setTrainName("El Gavilan");
				train3.setTrainScore(4.5);
				train3.setTrainType(1.5);
				
				route = new Route();
				route.setRouteId(1);
				route.setSource("Ciudad Guzman");
				route.setDestination("Guadalajara");
				route.setRouteCost(500.0);
				
				route2 = Optional.of(route);
				
				trainOp1 = Optional.of(train);
				trainOp2 = Optional.of(train2A);
				trainOp3 = Optional.of(train3);
				
				trip = new Trips();
				trip.setTripId(1);
				trip.setRouteId(1);
				trip.setTrainId(1);
				trip.setTripCost(route.getRouteCost()*train.getTrainType());
				trip.setTripDate(LocalDate.parse("2022-03-28"));
				
				trip2 = new Trips();
				trip2.setTripId(2);
				trip2.setRouteId(1);
				trip2.setTrainId(2);
				trip2.setTripCost(route.getRouteCost()*train2A.getTrainType());
				trip2.setTripDate(LocalDate.parse("2022-03-28"));
				
				trip3 = new Trips();
				trip3.setTripId(3);
				trip3.setRouteId(1);
				trip3.setTrainId(3);
				trip3.setTripCost(route.getRouteCost()*train3.getTrainType());
				trip3.setTripDate(LocalDate.parse("2022-03-28"));
				
				tripsList = new ArrayList<>();
				tripsList.add(trip);
				tripsList.add(trip2);
				tripsList.add(trip3);
				
		        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		        validator = factory.getValidator();
		        
		        paging = PageRequest.of(0,5);
		        
		        pageList = new PageImpl<Trips>(tripsList, paging, tripsList.size());
		        
		        emptyList = new ArrayList<>();
		        emptyPage = new PageImpl<Trips>(emptyList, paging, emptyList.size());
		
		
	}
	
	
	
//ALL TRAINS
	
	@Test
	@DisplayName("Get all Trains details: positive")
	public void getAllStoreDetails1()
	{
		when(trainRepository.findAll(paging)).thenReturn(pagelist);
		TrainResponseDTO trainResponseDTO = trainServiceImpl.getAllTrainsDetails(0,5);
		assertNotNull(trainResponseDTO);
		
	}
	
	@Test
	@DisplayName("Get all Trains details: negative")
	public void saveProductDetailsTest2() {
		when(trainRepository.findAll(paging)).thenReturn(pagelist2);
		assertThrows(TrainListEmptyException.class, () -> trainServiceImpl.getAllTrainsDetails(0, 5));
	}
	
	
//ALL DETAILS OF TRAIN
	
	@Test
	@DisplayName("Get Train details: positive")
	public void getTrainDetailsPositive()
	{
		when(trainRepository.findById(1)).thenReturn(trainTargetOp);
		TrainAllResponseDTO trainResponseDTO = trainServiceImpl.getTrainAllDetails(1);
		assertNotNull(trainResponseDTO);
		
	}
	
	@Test
	@DisplayName("Get Train details: negative")
	public void getTrainDetailsNegative()
	{
		when(trainRepository.findById(2)).thenReturn(trainTargetOp2);
		TrainAllResponseDTO trainResponseDTO = trainServiceImpl.getTrainAllDetails(2);
		assertNotNull(trainResponseDTO);
		
	}
	

//TRIINS TRIPS AND ROUTES
	
	
	@Test
	@DisplayName("Positive use case")
	public void getTripsDetailsTest()
	{
		when(routeRepository.findBySourceAndDestination("Ciudad Guzman", "Guadalajara")).thenReturn(route);
		when(tripRepository.findByTripDateAndRouteId(LocalDate.parse("2022-03-28"),1,paging)).thenReturn(pageList);
		when(routeRepository.findById(1)).thenReturn(route2);
		when(trainRepository.findById(1)).thenReturn(trainOp1);
		when(trainRepository.findById(2)).thenReturn(trainOp2);
		when(trainRepository.findById(3)).thenReturn(trainOp3);
		
		TripResponseDTO tripResponseDTO = trainServiceImpl.getTripsDetails(tripRequestDTO, 0, 5);
		
		assertNotNull(tripResponseDTO);
		assertEquals(1, tripResponseDTO.getTripDetails().get(0).getTripId());
		assertEquals("Ciudad Guzman", tripResponseDTO.getTripDetails().get(0).getSource());
		assertEquals("Guadalajara", tripResponseDTO.getTripDetails().get(0).getDestination());
		assertEquals("El Tacoriendo", tripResponseDTO.getTripDetails().get(0).getTrainName());
		assertEquals("Ciudad Guzman", tripResponseDTO.getTripDetails().get(0).getSource());
		assertEquals("Guadalajara", tripResponseDTO.getTripDetails().get(0).getDestination());
		assertEquals(LocalDate.parse("2022-03-28"), tripResponseDTO.getTripDetails().get(0).getTripDate());
		assertEquals(600.0, tripResponseDTO.getTripDetails().get(0).getTripCost());
		assertEquals("Trips for the given source, destination and date, Fetch Success", tripResponseDTO.getResponseDTO().getMessage());
		assertEquals(200,tripResponseDTO.getResponseDTO().getStatusCode());
		
	}
	
	@Test
	@DisplayName("Trips negative case 1: No route")
	public void getTripsDetailsTest1()
	{
		when(routeRepository.findBySourceAndDestination("Manzanillo", "Ciudad Guzman")).thenReturn(null);
		
		assertThrows(RouteNotFoundException.class, () -> trainServiceImpl.getTripsDetails(tripRequestDTO2, 0, 5));
	}
	
	@Test
	@DisplayName("Trips negative case 2: No trip with given date")
	public void getTripsDetailsTest2()
	{
		when(routeRepository.findBySourceAndDestination("Ciudad Guzman", "Guadalajara")).thenReturn(route);
		when(tripRepository.findByTripDateAndRouteId(LocalDate.parse("2022-04-13"), 1, paging)).thenReturn(emptyPage);
		
		assertThrows(TripsNotFoundException.class, () -> trainServiceImpl.getTripsDetails(tripRequestDTO3, 0, 5));
	}
	
	@Test
	@DisplayName("Trip negative case 3: request empty arguments")
	public void getTripsDetailsTest4()
	{
		
		 Set<ConstraintViolation<TripRequestDTO>> validations = validator.validate(tripRequestDTO4);
		 assertFalse(validations.isEmpty());
	}
	
}


	
	
	
	
	
	
	

