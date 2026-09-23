package com.rportaldev.apicitassalon.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rportaldev.apicitassalon.dto.CitaRequestDTO;
import com.rportaldev.apicitassalon.dto.CitaResponseDTO;
import com.rportaldev.apicitassalon.dto.ServicioResponseDTO;
import com.rportaldev.apicitassalon.entity.Cita;
import com.rportaldev.apicitassalon.entity.Profesional;
import com.rportaldev.apicitassalon.entity.Servicio;
import com.rportaldev.apicitassalon.entity.Usuario;
import com.rportaldev.apicitassalon.enums.EstadoCita;
import com.rportaldev.apicitassalon.exception.CancelacionNoPermitidaException;
import com.rportaldev.apicitassalon.exception.HorarioNoDisponibleException;
import com.rportaldev.apicitassalon.exception.ProfesionalNoCapacitadoException;
import com.rportaldev.apicitassalon.exception.RecursoNoEncontradoException;
import com.rportaldev.apicitassalon.repository.CitaRepository;
import com.rportaldev.apicitassalon.repository.ProfesionalRepository;
import com.rportaldev.apicitassalon.repository.ServicioRepository;
import com.rportaldev.apicitassalon.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CitaService {

	private final CitaRepository citaRepository;
	private final ProfesionalRepository profesionalRepository;
	private final ServicioRepository servicioRepository;
	private final UsuarioRepository usuarioRepository;
	
	public CitaResponseDTO crearCita(String correoCliente, CitaRequestDTO dto) {

		Usuario cliente = usuarioRepository.findByCorreo(correoCliente)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Usuario no encontrado con correo: " + correoCliente));

		Profesional profesional = profesionalRepository.findById(dto.getProfesionalId())
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Profesional no encontrado con ID: " + dto.getProfesionalId()));

		List<Servicio> servicios = servicioRepository.findAllById(dto.getServicioIds());

		if (servicios.size() != dto.getServicioIds().size()) {
			throw new RecursoNoEncontradoException("Uno o más servicios solicitados no existen");
		}

		List<Long> idsDelProfesional = profesional.getServicios()
				.stream()
				.map(Servicio::getId)
				.toList();

		if (!idsDelProfesional.containsAll(dto.getServicioIds())) {
			throw new ProfesionalNoCapacitadoException(
					"El profesional seleccionado no puede realizar todos los servicios solicitados");
		}

		int duracionTotal = servicios.stream()
				.mapToInt(Servicio::getDuracionMinuto)
				.sum();

		BigDecimal precioTotal = servicios.stream()
				.map(Servicio::getPrecio)
				.reduce(BigDecimal.ZERO, BigDecimal::add);

		LocalDateTime fechaInicio = dto.getFechaHoraInicio();
		LocalDateTime fechaFin = fechaInicio.plusMinutes(duracionTotal);

		List<Cita> citasExistentes = citaRepository.findByProfesionalIdAndEstado(
				profesional.getId(), EstadoCita.CONFIRMADA);

		boolean hayCruce = citasExistentes.stream()
				.anyMatch(citaExistente ->
						fechaInicio.isBefore(citaExistente.getFechaHorarioFin())
						&& fechaFin.isAfter(citaExistente.getFechaHorarioInicio()));

		if (hayCruce) {
			throw new HorarioNoDisponibleException(
					"El profesional ya tiene una cita en ese horario");
		}

		Cita cita = new Cita();
		cita.setCliente(cliente);
		cita.setProfesional(profesional);
		cita.setServicios(servicios);
		cita.setFechaHorarioInicio(fechaInicio);
		cita.setFechaHorarioFin(fechaFin);
		cita.setPrecioTotal(precioTotal);
		cita.setEstado(EstadoCita.CONFIRMADA);

		citaRepository.save(cita);

		return convertirADTO(cita);
	}
	
	
	
	public void cancelarCita(String correoCliente, Long citaId) {

		Cita cita = citaRepository.findById(citaId)
				.orElseThrow(() -> new RecursoNoEncontradoException(
						"Cita no encontrada con ID: " + citaId));

		if (!cita.getCliente().getCorreo().equals(correoCliente)) {
			throw new RecursoNoEncontradoException("Cita no encontrada con ID: " + citaId);
		}

		LocalDateTime limiteCancelacion = cita.getFechaHorarioInicio().minusHours(2);

		if (LocalDateTime.now().isAfter(limiteCancelacion)) {
			throw new CancelacionNoPermitidaException(
					"No se puede cancelar la cita con menos de 2 horas de anticipación");
		}

		cita.setEstado(EstadoCita.CANCELADA);
		citaRepository.save(cita);
	}

	
	
	public List<CitaResponseDTO> listarMisCitas(String correoCliente) {

		return citaRepository.findByClienteCorreo(correoCliente)
				.stream()
				.map(this::convertirADTO)
				.toList();
	}

	
	
	public List<CitaResponseDTO> listarCitasDeProfesional(String correoProfesional) {

		return citaRepository.findByProfesionalUsuarioCorreo(correoProfesional)
				.stream()
				.map(this::convertirADTO)
				.toList();
	}

	
	public List<CitaResponseDTO> listarTodasLasCitas() {

		return citaRepository.findAll()
				.stream()
				.map(this::convertirADTO)
				.toList();
	}
	
	
	private CitaResponseDTO convertirADTO(Cita cita) {

		List<ServicioResponseDTO> servicios = cita.getServicios()
				.stream()
				.map(servicio -> new ServicioResponseDTO(
						servicio.getId(),
						servicio.getNombre(),
						servicio.getDuracionMinuto(),
						servicio.getPrecio()))
				.toList();

		return new CitaResponseDTO(
				cita.getId(),
				cita.getCliente().getCorreo(),
				cita.getProfesional().getUsuario().getNombre(),
				cita.getFechaHorarioInicio(),
				cita.getFechaHorarioFin(),
				cita.getPrecioTotal(),
				cita.getEstado().name(),
				servicios);
	}
}
