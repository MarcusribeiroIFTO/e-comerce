package pweb2.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.Produto;
import pweb2.model.repository.DepartamentoRepository;
import pweb2.model.repository.ProdutoRepository;

import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private CarrinhoSession carrinhoSession;

    @GetMapping
    public String listar(@RequestParam(required = false) String nome,
                         @RequestParam(required = false) Long departamentoId,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "20") int size,
                         Model model) {

        List<Produto> produtos;
        long totalElementos;

        if (departamentoId != null){
            produtos = produtoRepository.findByDepartamentoId(departamentoId, page, size);
            totalElementos = produtoRepository.countByDepartamentoID(departamentoId);
        } else if (nome != null && !nome.isEmpty()) {
            produtos = produtoRepository.findByDescricaoContainingIgnoreCase(nome, null, page, size);
            totalElementos = produtoRepository.countByDescricaoContainingIgnoreCase(nome, null);
        }else {
            produtos = produtoRepository.findAll(page, size);
            totalElementos = produtoRepository.countAll();
        }

        int totalPaginas = (int) Math.ceil((double)totalElementos / size);

        model.addAttribute("produtos", produtos);
        model.addAttribute("carrinho", carrinhoSession.getCarrinho());
        model.addAttribute("nome", nome);
        model.addAttribute("departamentos", departamentoRepository.findAll());
        model.addAttribute("departamentoId", departamentoId);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPaginas", totalPaginas);
        model.addAttribute("pageSeze", size);
        return "produto/lista";
    }
    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("produto", new Produto());
        model.addAttribute("departamentos", departamentoRepository.findAll());
        return "produto/form";
    }
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

    @PostMapping("/salvar")
    public String salvar(@ModelAttribute Produto produto) {
        produtoRepository.save(produto);
        return "redirect:/produtos";
    }

    @GetMapping("/deletar/{id}")
    public String excluir(@PathVariable Long id) {
        produtoRepository.excluir(id);
        return "redirect:/produtos";
    }
    @GetMapping("/carrinho")
    public String verCarrinho(Model model){
        model.addAttribute("carrinho", carrinhoSession.getCarrinho());
        return "carinho";
    }
    @GetMapping("/remover-do-carrinho/{produtoId}")
    public String removerDoCarrinho(@PathVariable Long produtoId){
        Produto produto = produtoRepository.findById(produtoId);
        if (produto != null){
            carrinhoSession.getCarrinho().removerItem(produto);
        }
        return "redirect:/produtos/carrinho";
    }

}
