package pweb2.controll;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pweb2.model.entity.Departamento;
import pweb2.model.repository.DepartamentoRepository;

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
        Departamento departamento = departamentoRepository.buscarPorId(id);
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
}
