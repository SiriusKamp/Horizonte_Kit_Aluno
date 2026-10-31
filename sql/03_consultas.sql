-- TODO_ALUNO T1 E2 e E3. Substitua este bloco pelas duas consultas.
-- E2: pedido_id, cliente, estado, total histórico; ordenar por pedido_id.
-- E3: id, nome, preco_atual; categoria TRILHA e preço inclusivo 80..120.
BEGIN; 
SELECT A.ID,A.ESTADO,A.VERSAO,SUM(B.PRECO_UNITARIO*QUANTIDADE) AS VALOR_TOTAL,C.NOME FROM PEDIDO A INNER JOIN ITEM_PEDIDO B ON A.ID = B.PEDIDO_ID INNER JOIN CLIENTE C ON C.ID = A.CLIENTE_ID GROUP BY A.ID,A.ESTADO,A.VERSAO,C.NOME;
    
END;
