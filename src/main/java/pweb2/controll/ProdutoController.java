package pweb2.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.Produto;
import pweb2.model.repository.DepartamentoRepository;
import pweb2.model.repository.ProdutoRepository;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    // ================= LISTAGEM (PÚBLICA / USER / ADMIN) =================
    @GetMapping
    public String listar(@RequestParam(required = false) String nome,
                         @RequestParam(required = false) Long departamentoId,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {

        List<Produto> produtos;
        long totalElementos;

        if (departamentoId != null) {
            produtos = produtoRepository.findByDepartamentoId(departamentoId, page, size);
            totalElementos = produtoRepository.countByDepartamentoID(departamentoId);

        } else if (nome != null && !nome.isEmpty()) {
            produtos = produtoRepository.findByDescricaoContainingIgnoreCase(nome, null, page, size);
            totalElementos = produtoRepository.countByDescricaoContainingIgnoreCase(nome, null);

        } else {
            produtos = produtoRepository.findAll(page, size);
            totalElementos = produtoRepository.countAll();
        }

        int totalPaginas = (int) Math.ceil((double) totalElementos / size);

        model.addAttribute("produtos", produtos);
        model.addAttribute("departamentos", departamentoRepository.findAll());
        model.addAttribute("nome", nome);
        model.addAttribute("departamentoId", departamentoId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPaginas", totalPaginas);
        model.addAttribute("pageSize", size);

        return "produto/lista";
    }

    // ================= NOVO PRODUTO (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("departamentos", departamentoRepository.findAll());
        return "produto/form";
    }

    // ================= EDITAR (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Produto produto = produtoRepository.findById(id);
        if (produto == null) {
            return "redirect:/produtos";
        }

        model.addAttribute("produto", produto);
        model.addAttribute("departamentos", departamentoRepository.findAll());
        return "produto/form";
    }

    // ================= SALVAR (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto) {
        produtoRepository.save(produto);
        return "redirect:/produtos";
    }

    // ================= EXCLUIR (ADMIN) =================
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deletar/{id}")
    public String excluir(@PathVariable Long id) {
        produtoRepository.excluir(id);
        return "redirect:/produtos";
    }

    // ================= DETALHES (TODOS) =================
    @GetMapping("/detalhes/{id}")
    public String detalhes(@PathVariable Long id, Model model) {

        Optional<Produto> optional =
                Optional.ofNullable(produtoRepository.findById(id));

        if (optional.isPresent()) {
            model.addAttribute("produto", optional.get());
            return "produto/detalhes";
        }

        return "redirect:/produtos";
    }
}