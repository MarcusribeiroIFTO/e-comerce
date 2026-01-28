package pweb2.controll;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.Departamento;
import pweb2.model.repository.DepartamentoRepository;

import java.util.List;

@Controller
@RequestMapping("/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @GetMapping("/novo")
    public String novoDepartamento(Model model){
        model.addAttribute("departamento", new Departamento());
        return "departamento/form";
    }
    @GetMapping("/salvar")
    public String salvarDepartamento(@Valid @ModelAttribute Departamento departamento, BindingResult result, Model model){
        if (result.hasErrors()){
            model.addAttribute("departamento", departamento);
            return "departamento/form";
        }
        if (departamento.getId() == null){
            departamentoRepository.salvar(departamento);
        }else {
            departamentoRepository.salvar(departamento);
        }
        return "redirect:/departamentos";
    }
    @GetMapping("/editar/{id}")
    public String editarDepartamento(@PathVariable Long id, Model model){
        Departamento departamento = departamentoRepository.findById(id);
        if (departamento == null){
                throw  new IllegalArgumentException("Departamento não encontrado id: " + id);
        }
        model.addAttribute("departamento", departamento);
        return "departamento/form";
    }
    @GetMapping("/excluir/{id}")
    public String excluirDepartamento(@PathVariable Long id){
        departamentoRepository.remover(id);
        return "redirect:/departamentos";
    }
    @GetMapping("/listar")
    public String listarDepartamentos(@RequestParam(required = false) String nome, Model model){
        List<Departamento> departamentos;
        if (nome != null && !nome.trim().isEmpty()){
            departamentos =  departamentoRepository.findByNomeContainingIgnoreCase(nome);
        }else {
            departamentos =  departamentoRepository.findAll();
        }

        model.addAttribute("departamentos", departamentos);
        model.addAttribute("nome", nome);
        return "departamento/list";
    }
}
