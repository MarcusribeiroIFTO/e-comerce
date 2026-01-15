package pweb2.controll;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.PessoaFisica;
import pweb2.model.repository.PessoaRepository;

@Controller
@RequestMapping("/pf")
public class PessoaFisicaController {

    private final PessoaRepository pessoaRepository;

    public PessoaFisicaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pessoas", pessoaRepository.findAllFisicas());
        return "pessoas/pf_list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("pessoa", new PessoaFisica());
        return "pessoas/pf_form";
    }

    @PostMapping("/salvar")
    public String salvar(PessoaFisica pessoa) {
        pessoaRepository.save(pessoa);
        return "redirect:/pf";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        PessoaFisica p = (PessoaFisica) pessoaRepository.findById(id);
        model.addAttribute("pessoa", p);
        return "pessoas/pf_form";
    }


    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        pessoaRepository.delete(id);
        return "redirect:/pf";
    }
}
