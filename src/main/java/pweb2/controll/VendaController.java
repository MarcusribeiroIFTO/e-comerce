package pweb2.controll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pweb2.model.entity.*;
import pweb2.model.repository.PessoaRepository;
import pweb2.model.repository.ProdutoRepository;
import pweb2.model.repository.VendaRepository;

import java.net.Authenticator;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/vendas")
@SessionAttributes("venda")
public class VendaController {


    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final PessoaRepository pessoaRepository;
    private final CarrinhoSession carrinhoSession;

    public VendaController(VendaRepository vendaRepository,
                           ProdutoRepository produtoRepository,
                           PessoaRepository pessoaRepository,
                           CarrinhoSession carrinhoSession) {
        this.vendaRepository = vendaRepository;
        this.produtoRepository = produtoRepository;
        this.pessoaRepository = pessoaRepository;
        this.carrinhoSession = carrinhoSession;
    }

    @ModelAttribute("venda") //inicializar a sessão da venda
    public Venda vendaSetup() {
        Venda venda = new Venda();
        venda.setItens(new ArrayList<>());
        return venda;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("vendas", vendaRepository.findAll());
        return "list";
    }

    @GetMapping("/novo")
    public String novo(Model model, @ModelAttribute("venda")Venda venda) {
        model.addAttribute("clientes", pessoaRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll(0, Integer.MAX_VALUE));
        return "venda/form";
    }
    @PostMapping("/adicionar-item")
    public String adicionarItem(@RequestParam Long produtoId,
                                @RequestParam Integer quantidade,
                                @ModelAttribute("venda") Venda venda,
                                Model model) {

        Produto produto = produtoRepository.findById(produtoId);

        if (produto != null) {
            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(quantidade);
            item.setVenda(venda);
            venda.getItens().add(item);
        }
        model.addAttribute("clientes",  pessoaRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll(0, Integer.MAX_VALUE));

        return "venda/form";
    }

    @PostMapping("/salvar")
    public String salvar(@RequestParam Long clienteId,
                         @ModelAttribute("venda") Venda venda,
                         SessionStatus status) {

        Pessoa cliente = pessoaRepository.findById(clienteId);
        venda.setCliente(cliente);

        for (ItemVenda item : venda.getItens()) {
            item.setVenda(venda);
        }

        vendaRepository.salvar(venda);
        status.setComplete();// atributo para limpar a sessão

        return "redirect:/vendas";
    }

    @GetMapping("/deletarItem")
    public String deletarItem(@RequestParam int index,
                              @ModelAttribute("venda") Venda venda,
                              Model model) {
        if (index >=0 && index < venda.getItens().size()){
            venda.getItens().remove(index);
        }
        model.addAttribute("clientes",  pessoaRepository.findAll());
        model.addAttribute("produtos", produtoRepository.findAll(0, Integer.MAX_VALUE));
        return "venda/form";
    }

    @GetMapping("/{id}")
    public String detalhes(@PathVariable Long id, Model model) {
        Venda venda = vendaRepository.findById(id);
        if (venda == null) {
            return "redirect:/vendas";
        }
        model.addAttribute("venda", venda);
        return "detalhes";
    }

    public String Filtrar(@RequestParam(required = false) String nomeCliente,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                          Model model){

        List <Venda> vendas;

        if (nomeCliente != null && !nomeCliente.trim().isEmpty() && inicio !=null && fim != null){
            List<Pessoa> clientes = pessoaRepository.findByNomeOuRazaoSocial(nomeCliente);
            LocalDateTime dataInicio = inicio.atStartOfDay();
            LocalDateTime dataFim =  fim.atTime(LocalTime.MAX);
            vendas = vendaRepository.buscarPorClientesEData(clientes, dataInicio,dataFim);

        } else if (nomeCliente != null && !nomeCliente.trim().isEmpty()) {
            List<Pessoa> clientes = pessoaRepository.findByNomeOuRazaoSocial(nomeCliente);
            vendas = vendaRepository.buscarPorClientes(clientes);

        } else if (inicio != null && fim != null) {
                LocalDateTime dataInicio = inicio.atStartOfDay();
                LocalDateTime dataFim = fim.atTime(LocalTime.MAX);
                vendas = vendaRepository.buscarPorData(dataInicio, dataFim);

        }else{
            vendas = vendaRepository.findAll();
        }

        model.addAttribute("vendas", vendas);
        model.addAttribute("nomeCLiente", nomeCliente);
        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);
        model.addAttribute("filtroAtivo", true);
        model.addAttribute("clientes", pessoaRepository.findAll());
        return "list";
    }
    @GetMapping("/cliente/{clienteId}")
    public String vendasPorCliente(@PathVariable Long clienteId,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fim,
                                   Model model){
        Pessoa cliente = pessoaRepository.findById(clienteId);
        if (cliente == null){
            return "redirect:/clientes";
        }
        List<Venda> vendas;
        if (inicio != null && fim != null){
            LocalDateTime dataInicio = inicio.atStartOfDay();
            LocalDateTime dataFim = fim.atTime(LocalTime.MAX);
            vendas = vendaRepository.buscarPorClienteEData(cliente, dataInicio, dataFim);
        }else {
            vendas = vendaRepository.buscarPorCliente(cliente);
        }
        model.addAttribute("cliente", cliente);
        model.addAttribute("vendas", vendas);
        model.addAttribute("inicio", inicio);
        model.addAttribute("fim", fim);

        return "venda/vendas-por-cliente";
    }
    @GetMapping("/minhas-compras")
    public String minhasCompras(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();

        Pessoa cliente = pessoaRepository.findByEmail(userName);
        if (cliente == null){
            return "redirect:/login?error=clientNotFound";
        }

        List<Venda> minhasVendas = vendaRepository.buscarPorCliente(cliente);
        model.addAttribute("minhasVendas", minhasVendas);

        return "venda/minhas-compras";
    }
    @GetMapping("/finalizar")
    public String finalizar(){
        Carrinho carrinho = carrinhoSession.getCarrinho();
        if (carrinho.getItens().isEmpty()){
            return "redirect:/produtos";
        }
        Venda venda = new Venda();
        venda.setDataVenda(LocalDateTime.now());

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String username;

        if (principal instanceof UserDetails){
            username = ((UserDetails)principal).getUsername();
        }else {
            username = principal.toString();
        }

        Pessoa cliente = pessoaRepository.findByEmail(username);
        if (cliente == null){
            return "redirect:/login?error=clienteNotDound";
        }
        venda.setCliente(cliente);

        List<ItemVenda> itensVenda = new ArrayList<>();
        for (ItemVenda carrinhoItem : carrinho.getItens()){
            Produto managedProduto = produtoRepository.findById(carrinhoItem.getProduto().getId());

            ItemVenda newItemVenda = new ItemVenda();
            newItemVenda.setId(null);
            newItemVenda.setProduto(managedProduto);
            newItemVenda.setQuantidade(carrinhoItem.getQuantidade());
            newItemVenda.setVenda(venda);
            itensVenda.add(newItemVenda);
        }
        venda.setItens(itensVenda);

        vendaRepository.salvar(venda);
        carrinho.limpar();
        return "venda/sucesso";
    }



}
