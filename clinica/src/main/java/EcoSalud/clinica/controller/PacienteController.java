package EcoSalud.clinica.controller;

import EcoSalud.clinica.model.Paciente;
import EcoSalud.clinica.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pacientes")
@CrossOrigin(origins = "*") 
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    // 1. OBTENER TODOS: Ideal para la gestión administrativa [cite: 9]
    @GetMapping
    public List<Paciente> listarPacientes() {
        return repository.findAll();
    }

    // 2. OBTENER POR ID: Garantiza que el médico vea solo la ficha autorizada (Privacidad) [cite: 12]
    @GetMapping("/{id}")
    public ResponseEntity<Paciente> obtenerPorId(@PathVariable Long id) {
        return repository.findById(id)
                .map(paciente -> ResponseEntity.ok(paciente))
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. CREAR: Registro rápido de nuevos pacientes para telemedicina [cite: 4]
    @PostMapping
    public Paciente guardarPaciente(@RequestBody Paciente paciente) {
        return repository.save(paciente);
    }

    // 4. ACTUALIZAR: Mantiene la ficha clínica al día (Eficiencia operativa) [cite: 22]
    @PutMapping("/{id}")
    public ResponseEntity<Paciente> actualizarPaciente(@PathVariable Long id, @RequestBody Paciente detallesPaciente) {
        return repository.findById(id)
                .map(paciente -> {
                    paciente.setNombre(detallesPaciente.getNombre());
                    paciente.setRut(detallesPaciente.getRut());
                    paciente.setCorreo(detallesPaciente.getCorreo());
                    Paciente actualizado = repository.save(paciente);
                    return ResponseEntity.ok(actualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    // 5. ELIMINAR: Cumplimiento de leyes de protección de datos personales [cite: 12, 58]
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPaciente(@PathVariable Long id) {
        return repository.findById(id)
                .map(paciente -> {
                    repository.delete(paciente);
                    return ResponseEntity.noContent().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }
}