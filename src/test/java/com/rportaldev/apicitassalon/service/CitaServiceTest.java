package com.rportaldev.apicitassalon.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.rportaldev.apicitassalon.dto.CitaRequestDTO;
import com.rportaldev.apicitassalon.dto.CitaResponseDTO;
import com.rportaldev.apicitassalon.entity.Cita;
import com.rportaldev.apicitassalon.entity.Profesional;
import com.rportaldev.apicitassalon.entity.Servicio;
import com.rportaldev.apicitassalon.entity.Usuario;
import com.rportaldev.apicitassalon.enums.EstadoCita;
import com.rportaldev.apicitassalon.enums.RolUsuario;
import com.rportaldev.apicitassalon.exception.CancelacionNoPermitidaException;
import com.rportaldev.apicitassalon.exception.HorarioNoDisponibleException;
import com.rportaldev.apicitassalon.exception.ProfesionalNoCapacitadoException;
import com.rportaldev.apicitassalon.exception.RecursoNoEncontradoException;
import com.rportaldev.apicitassalon.repository.CitaRepository;
import com.rportaldev.apicitassalon.repository.ProfesionalRepository;
import com.rportaldev.apicitassalon.repository.ServicioRepository;
import com.rportaldev.apicitassalon.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class CitaServiceTest {

	@Mock
	private CitaRepository citaRepository;

	@Mock
	private ProfesionalRepository profesionalRepository;

	@Mock
	private ServicioRepository servicioRepository;

	@Mock
	private UsuarioRepository usuarioRepository;

	@InjectMocks
	private CitaService citaService;

	private Usuario cliente;
	private Profesional profesional;
	private Servicio corte;
	private Servicio tinte;

	@BeforeEach
	void setUp() {

		cliente = new Usuario();
		cliente.setId(1L);
		cliente.setCorreo("maria@test.com");
		cliente.setRol(RolUsuario.ROLE_CLIENTE);

		corte = new Servicio();
		corte.setId(1L);
		corte.setNombre("Corte de cabello");
		corte.setDuracionMinuto(30);
		corte.setPrecio(new BigDecimal("8.00"));

		tinte = new Servicio();
		tinte.setId(3L);
		tinte.setNombre("Tinte");
		tinte.setDuracionMinuto(120);
		tinte.setPrecio(new BigDecimal("25.00"));

		Usuario usuarioProfesional = new Usuario();
		usuarioProfesional.setId(2L);
		usuarioProfesional.setNombre("Estilista Dos");
		usuarioProfesional.setCorreo("estilista2@salon.com");

		profesional = new Profesional();
		profesional.setId(2L);
		profesional.setUsuario(usuarioProfesional);
		profesional.setDiaInicio(DayOfWeek.SATURDAY);
		profesional.setDiaFin(DayOfWeek.SUNDAY);
		profesional.setHoraInicio(LocalTime.of(9, 0));
		profesional.setHoraFin(LocalTime.of(18, 0));
		profesional.setServicios(List.of(corte, tinte));
	}
	
	
	@Test
	void crearCita_debeCalcularTotalYHoraFin_siTodoEsValido() {

		CitaRequestDTO dto = new CitaRequestDTO();
		dto.setProfesionalId(2L);
		dto.setServicioIds(List.of(1L, 3L));
		dto.setFechaHoraInicio(LocalDateTime.of(2026, 9, 26, 10, 0));

		when(usuarioRepository.findByCorreo("maria@test.com")).thenReturn(Optional.of(cliente));
		when(profesionalRepository.findById(2L)).thenReturn(Optional.of(profesional));
		when(servicioRepository.findAllById(List.of(1L, 3L))).thenReturn(List.of(corte, tinte));
		when(citaRepository.findByProfesionalIdAndEstado(2L, com.rportaldev.apicitassalon.enums.EstadoCita.CONFIRMADA))
				.thenReturn(List.of());

		CitaResponseDTO resultado = citaService.crearCita("maria@test.com", dto);

		assertEquals(new BigDecimal("33.00"), resultado.getPrecioTotal());
		assertEquals(LocalDateTime.of(2026, 9, 26, 12, 30), resultado.getFechaHorarioFin());
	}
	
	
	@Test
	void crearCita_debeLanzarExcepcion_siUsuarioNoExiste() {

		CitaRequestDTO dto = new CitaRequestDTO();
		dto.setProfesionalId(2L);
		dto.setServicioIds(List.of(1L));
		dto.setFechaHoraInicio(LocalDateTime.of(2026, 9, 26, 10, 0));

		when(usuarioRepository.findByCorreo("fantasma@test.com")).thenReturn(Optional.empty());

		assertThrows(RecursoNoEncontradoException.class,
				() -> citaService.crearCita("fantasma@test.com", dto));
	}
	
	
	@Test
	void crearCita_debeLanzarExcepcion_siProfesionalNoExiste() {

		CitaRequestDTO dto = new CitaRequestDTO();
		dto.setProfesionalId(99L);
		dto.setServicioIds(List.of(1L));
		dto.setFechaHoraInicio(LocalDateTime.of(2026, 9, 26, 10, 0));

		when(usuarioRepository.findByCorreo("maria@test.com")).thenReturn(Optional.of(cliente));
		when(profesionalRepository.findById(99L)).thenReturn(Optional.empty());

		assertThrows(RecursoNoEncontradoException.class,
				() -> citaService.crearCita("maria@test.com", dto));
	}
	
	
	@Test
	void crearCita_debeLanzarExcepcion_siServicioNoExiste() {

		CitaRequestDTO dto = new CitaRequestDTO();
		dto.setProfesionalId(2L);
		dto.setServicioIds(List.of(1L, 99L));
		dto.setFechaHoraInicio(LocalDateTime.of(2026, 9, 26, 10, 0));

		when(usuarioRepository.findByCorreo("maria@test.com")).thenReturn(Optional.of(cliente));
		when(profesionalRepository.findById(2L)).thenReturn(Optional.of(profesional));
		when(servicioRepository.findAllById(List.of(1L, 99L))).thenReturn(List.of(corte));

		assertThrows(RecursoNoEncontradoException.class,
				() -> citaService.crearCita("maria@test.com", dto));
	}
	
	
	@Test
	void crearCita_debeLanzarExcepcion_siProfesionalNoCapacitado() {

		Servicio manicure = new Servicio();
		manicure.setId(2L);
		manicure.setNombre("Manicure");
		manicure.setDuracionMinuto(45);
		manicure.setPrecio(new BigDecimal("10.00"));

		CitaRequestDTO dto = new CitaRequestDTO();
		dto.setProfesionalId(2L);
		dto.setServicioIds(List.of(1L, 2L));
		dto.setFechaHoraInicio(LocalDateTime.of(2026, 9, 26, 10, 0));

		when(usuarioRepository.findByCorreo("maria@test.com")).thenReturn(Optional.of(cliente));
		when(profesionalRepository.findById(2L)).thenReturn(Optional.of(profesional));
		when(servicioRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(corte, manicure));

		assertThrows(ProfesionalNoCapacitadoException.class,
				() -> citaService.crearCita("maria@test.com", dto));
	}
	
	
	@Test
	void crearCita_debeLanzarExcepcion_siHayCruceDeHorario() {

		Cita citaExistente = new Cita();
		citaExistente.setFechaHorarioInicio(LocalDateTime.of(2026, 9, 26, 9, 0));
		citaExistente.setFechaHorarioFin(LocalDateTime.of(2026, 9, 26, 11, 0));

		CitaRequestDTO dto = new CitaRequestDTO();
		dto.setProfesionalId(2L);
		dto.setServicioIds(List.of(1L));
		dto.setFechaHoraInicio(LocalDateTime.of(2026, 9, 26, 10, 0));

		when(usuarioRepository.findByCorreo("maria@test.com")).thenReturn(Optional.of(cliente));
		when(profesionalRepository.findById(2L)).thenReturn(Optional.of(profesional));
		when(servicioRepository.findAllById(List.of(1L))).thenReturn(List.of(corte));
		when(citaRepository.findByProfesionalIdAndEstado(2L, EstadoCita.CONFIRMADA))
				.thenReturn(List.of(citaExistente));

		assertThrows(HorarioNoDisponibleException.class,
				() -> citaService.crearCita("maria@test.com", dto));
	}
	
	
	@Test
	void cancelarCita_debeCancelar_siFaltaMasDeDosHoras() {

		Cita cita = new Cita();
		cita.setId(1L);
		cita.setCliente(cliente);
		cita.setFechaHorarioInicio(LocalDateTime.now().plusHours(5));
		cita.setEstado(EstadoCita.CONFIRMADA);

		when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

		citaService.cancelarCita("maria@test.com", 1L);

		assertEquals(EstadoCita.CANCELADA, cita.getEstado());
	}
	
	
	@Test
	void cancelarCita_debeLanzarExcepcion_siFaltaMenosDeDosHoras() {

		Cita cita = new Cita();
		cita.setId(1L);
		cita.setCliente(cliente);
		cita.setFechaHorarioInicio(LocalDateTime.now().plusMinutes(30));
		cita.setEstado(EstadoCita.CONFIRMADA);

		when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

		assertThrows(CancelacionNoPermitidaException.class,
				() -> citaService.cancelarCita("maria@test.com", 1L));
	}
	
	
	@Test
	void cancelarCita_debeLanzarExcepcion_siNoPerteneceAlCliente() {

		Usuario otroCliente = new Usuario();
		otroCliente.setCorreo("pedro@test.com");

		Cita cita = new Cita();
		cita.setId(1L);
		cita.setCliente(otroCliente);
		cita.setFechaHorarioInicio(LocalDateTime.now().plusHours(5));
		cita.setEstado(EstadoCita.CONFIRMADA);

		when(citaRepository.findById(1L)).thenReturn(Optional.of(cita));

		assertThrows(RecursoNoEncontradoException.class,
				() -> citaService.cancelarCita("maria@test.com", 1L));
	}
}