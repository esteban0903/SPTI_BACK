package co.edu.eci.blueprints.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blueprints")
public class BlueprintController {

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_blueprints.read')")
    public List<Map<String, String>> list() {
        return List.of(
            Map.of("id", "b1", "name", "Casa de campo"),
            Map.of("id", "b2", "name", "Edificio urbano")
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_blueprints.write')")
    public Map<String, String> create(@RequestBody Map<String, String> in) {
        return Map.of("id", "new", "name", in.getOrDefault("name", "nuevo"));
    }
}
