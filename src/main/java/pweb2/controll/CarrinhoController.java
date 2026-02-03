package pweb2.controll;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pweb2.model.entity.Produto;
import pweb2.model.repository.ProdutoRepository;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CarrinhoSession carrinhoSession;

    // ================= VER CARRINHO =================
    @GetMapping
    public String verCarrinho(Model model) {
        model.addAttribute("carrinho", carrinhoSession.getCarrinho());
        return "carrinho/index";
    }

    // ================= ADICIONAR AO CARRINHO =================
    @PostMapping("/adicionar/{id}")
    public String adicionar(@PathVariable Long id) {

        Produto produto = produtoRepository.findById(id);

        if (produto != null) {
            carrinhoSession.getCarrinho().adicionarProduto(produto, 1);
        }

        return "redirect:/produtos";
    }

    // ================= REMOVER ITEM =================
    @GetMapping("/remover/{id}")
    public String remover(@PathVariable Long id) {

        Produto produto = produtoRepository.findById(id);

        if (produto != null) {
            carrinhoSession.getCarrinho().removerItem(produto);
        }

        return "redirect:/carrinho";
    }

    // ================= LIMPAR CARRINHO =================
    @GetMapping("/limpar")
    public String limpar() {
        carrinhoSession.getCarrinho().limpar();
        return "redirect:/carrinho";
    }
}