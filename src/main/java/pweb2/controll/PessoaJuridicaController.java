package pweb2.controll;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.PessoaJuridica;
import pweb2.model.repository.PessoaRepository;

@Controller
@RequestMapping("/pj")
public class PessoaJuridicaController {

    private final PessoaRepository pessoaRepository;

    public PessoaJuridicaController(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("pessoas", pessoaRepository.findAllJuridicas());
        return "pessoas/pj_list";
    }

    @GetMapping("/novo")
    public String novoForm(Model model) {
        model.addAttribute("pessoa", new PessoaJuridica());
        return "pessoas/pj_form";
    }

    @PostMapping("/salvar")
    public String salvar(PessoaJuridica pessoa) {
        pessoaRepository.save(pessoa);
        return "redirect:/pj";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        var p = pessoaRepository.findById(id);
        model.addAttribute("pessoa", p);
        return "pessoas/pj_form";
    }

    @GetMapping("/deletar/{id}")
    public String deletar(@PathVariable Long id) {
        pessoaRepository.delete(id);
        return "redirect:/pj";
    }
}
