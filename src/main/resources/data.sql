
--CLIENTES
INSERT INTO tb_pessoa (id, email, telefone, senha, role)
values (1,'fulano@email.com','(63)99999-8888','$2a$10$L.ulTSyKR1RzNyblMde.2.Z.WjvD1FxCRlaP2XlVSHJiMbLzFAXAm','ROLE_USER');

INSERT INTO tb_pessoa_fisica (id, nome, cpf) VALUES (1,'Fulano Pereira',12345678900);

INSERT INTO tb_pessoa (id, email, telefone, senha, role)
values (2,'marcus@email.com','(63)99249-9591','$2a$10$mgvK2LisoyWih6OShC9n7uidTNmhaW3671nKLRdAx5bLRJgcp85mG','ROLE_ADMIN');

INSERT INTO tb_pessoa_juridica (id, razao_social, cnpj) VALUES (2,'Agropet',41301531000100);


--DEPARTAMENTOS
INSERT INTO tb_departamento (id, nome, descricao) values (1,'Ração para Cães','Rações para cães adultos e filhotes');
INSERT INTO tb_departamento (id, nome, descricao) values (2,'Ração para Gatos','Rações para gatos adultos e filhotes');
INSERT INTO tb_departamento (id, nome, descricao) values (3,'Medicamentos','Medicamentos e Anti-Inflamatorios');
INSERT INTO tb_departamento (id, nome, descricao) values (4,'Inseticidas','Controle de insetos');
INSERT INTO tb_departamento (id, nome, descricao) values (5,'Caixas de Transporte','Caixa para o transporte de animais');
INSERT INTO tb_departamento (id, nome, descricao) values (6,'Banho e Higiene','Shampoo');

--PRODUTOS
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Besser Natural Cães Adultos',88.50,'/img/produtos/besser-adul.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Besser Natural Cães Filhotes',99.50,'/img/produtos/besser-filhotes.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Fino Trato Gold Cães Adultos',143.80,'/img/produtos/ft-gold-caes-adul.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Besser Natural Cães Filhoes',158.60,'/img/produtos/ft-gold-caes-filhotes.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Fino Trato Gatos Adultos',138.60,'/img/produtos/ft-gold-gatos-adul.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Fino Trato Gatos Gastrados Adultos',153.20,'/img/produtos/ft-gold-gatos-adul-cast.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Fino Trato Gatos Filhotes',145.35,'/img/produtos/ft-gold-gatos-filhotes.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Fino Trato Gatos Select',156.70,'/img/produtos/ft-gold-gatos-select.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Calminex 30 Gr',37.80,'/img/produtos/calminex_30gr.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Metilvet 5mg',33.20,'/img/produtos/metilvet_5mg.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Prediderm 5mg',44.35,'/img/produtos/prediderm_5mg.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('TerraCan Spray 150ml',36.00,'/img/produtos/terracan.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('K-Othrine Pó',26.75,'/img/produtos/kothrine_po.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Poderoso 30,ml',18.00,'/img/produtos/poderoso_30ml.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Poderoso 150 ml',27.45,'/img/produtos/poderoso_150ml.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Poderoso Mata Mosca',6.85,'/img/produtos/poderoso_mosquicida.png');
INSERT INTO tb_produto (descricao, valor, image_Url)values ('Caixa Transporte N2 Rosa',66.35,'/img/produtos/caixa_transp_n2_rosa.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Caixa Transporte N2 Vermelha',66.35,'/img/produtos/caixa_transp_n2_verm.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Caixa Transporte N4 Vermelha',86.35,'/img/produtos/caixa_transp_n4_verm.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Caixa Transporte N4 Azul',86.35,'/img/produtos/caixa_transp_n4_azul.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Shampoo 6 em 1',27.35,'/img/produtos/shampoo_6em1.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Shampoo Branquedor',27.35,'/img/produtos/shampoo_branqueador.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Shampoo Escurecedor',27.35,'/img/produtos/shampoo_escurecedor.png');
INSERT INTO tb_produto (descricao, valor, image_Url) values ('Shampoo Filhotes',27.35,'/img/produtos/shampoo_filhotes.png');

--VENDAS
INSERT INTO tb_venda (id, data, cliente_id) VALUES (1,'2025-12-15',1);
INSERT INTO tb_venda (id, data, cliente_id) VALUES (2,'2025-12-14',2);
INSERT INTO tb_venda (id, data, cliente_id) VALUES (3,'2025-12-13',2);

-- ITENS DA VENDA
INSERT INTO tb_item_venda(quantidade, venda_id, produto_id) values (1,1,1); -- besser natural
INSERT INTO tb_item_venda(quantidade, venda_id, produto_id) values (2,2,24); -- shampoo
INSERT INTO tb_item_venda(quantidade, venda_id, produto_id) values (2,3,20); -- caixa de transporte
INSERT INTO tb_item_venda(quantidade, venda_id, produto_id) values (1,3,14); -- besser natural