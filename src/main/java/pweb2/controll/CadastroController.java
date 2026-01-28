package pweb2.controll;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.Pessoa;
import pweb2.model.entity.PessoaFisica;
import pweb2.model.entity.PessoaJuridica;
import pweb2.model.repository.PessoaRepository;

@Controller
@RequestMapping("/clientes")
public class CadastroController {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/novo")
    public String novoCliente(@RequestParam(required = false, defaultValue = "FISICA") String tipo, Model model){
        String tipoUpper = tipo.toUpperCase();
        model.addAttribute("tipo", tipoUpper);

        if ("juridica".equals(tipoUpper)){
            model.addAttribute("pessoa", new PessoaJuridica());
        }else {
            model.addAttribute("pessoa", new PessoaFisica());
        }

        return "clientes/form";
    }
    @PostMapping(value = "/salvar", params = "tipo=FISICA")
    public String salvarFisica(@Valid @ModelAttribute("pessoa") PessoaFisica pessoa,
                               BindingResult bindingResult, Model model){
        pessoa.setRole("ROLE_USER");
        if (pessoa.getSenha() == null || pessoa.getSenha().isBlank()){
            bindingResult.rejectValue("password", "error.passwprd", "a senha é obrigatoira");
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("tipo", "FISICA");
            return "clientes/form";
        }
        pessoa.setSenha(passwordEncoder.encode(pessoa.getSenha()));
        pessoaRepository.save(pessoa);
        return "redirect:/login";
    }
    @PostMapping(value = "/salvar", params = "tipo=JURIDICA")
    public String salvarJuridica(@Valid @ModelAttribute("pessoa") PessoaJuridica pessoa,
                               BindingResult bindingResult, Model model){
        pessoa.setRole("ROLE_USER");
        if (pessoa.getSenha() == null || pessoa.getSenha().isBlank()){
            bindingResult.rejectValue("password", "error.passwprd", "a senha é obrigatoira");
        }

        if (bindingResult.hasErrors()){
            model.addAttribute("tipo", "JURIDICA");
            return "clientes/form";
        }
        pessoa.setSenha(passwordEncoder.encode(pessoa.getSenha()));
        pessoaRepository.save(pessoa);
        return "redirect:/login";
    }

    @GetMapping("/listar")
    public String listarClientes(Model model){
        model.addAttribute("clientes", pessoaRepository.findAll());
        return "clientes/list";
    }

    @GetMapping("/editar/{id}")
    public String editarPessoa(@PathVariable Long id, Model model){
        Pessoa pessoa = pessoaRepository.findById(id);
        if (pessoa == null) return "redirect:/clientes";

        if (pessoa instanceof PessoaFisica pf){
            model.addAttribute("tipo", "FISICA");
            model.addAttribute("pessoa", pf);
        } else if (pessoa instanceof PessoaJuridica pj){
            model.addAttribute("tipo", "JURIDICA");
            model.addAttribute("pessoa", pj);
        }else {
            return "redirect:/clientes";
        }
        return "clientes/form";
    }

    @PostMapping(value = "/atualizar", params = "tipo=FISICA")
    public String atualizarFisica(@Valid @ModelAttribute("pessoa") PessoaFisica pessoa,
                                  BindingResult bindingResult, Model model){
        return atualizarPessoa(pessoa, "FISICA", bindingResult, model);
    }

    @PostMapping(value = "/atualizar", params = "tipo=JURIDICA")
    public String atualizarJuridica(@Valid @ModelAttribute("pessoa") PessoaJuridica pessoa,
                                  BindingResult bindingResult, Model model){
        return atualizarPessoa(pessoa, "JURIDICA", bindingResult, model);
    }

    private String atualizarPessoa( Pessoa pessoa, String tipo,
                                    BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()){
            model.addAttribute("tipo", tipo);
            return "clientes/form";
        }
        Pessoa pessoa1 = pessoaRepository.findById(pessoa.getId());
        pessoa.setSenha(pessoa1.getSenha());
        pessoa.setRole(pessoa1.getRole());

        pessoaRepository.update(pessoa);
        return "redirect:/clientes";
    }

    @GetMapping("/remove/{id}")
    public String remover(@PathVariable Long id){
        pessoaRepository.delete(id);
        return "redirect:/clientes";
    }
}
