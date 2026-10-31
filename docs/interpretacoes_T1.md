# Exercícios de interpretação de documentos chaves e grafos
Estes exercícios não exigem servidores adicionais. Leiam os dados e a consulta, confiram o resultado fornecido e expliquem sua semântica em duas a quatro frases. Não tratem as saídas fornecidas como execução realizada pelo grupo.
## T1 E5 Documento de catálogo
Considere a coleção produtos com três documentos: id 10 nome Mochila 25L atributos capacidade_l 25; id 20 nome Livro de SQL atributos paginas 240; id 30 nome Garrafa 1L atributos volume_l 1. Os demais campos não interferem na consulta.
```text
db.produtos.find(
  {"atributos.capacidade_l": {$gte: 20}},
  {_id: 0, id: 1, nome: 1}
).sort({id: 1})
```
Resultado fornecido: documento com id 10 e nome Mochila 25L. Expliquem o filtro, a projeção e por que o livro não é selecionado. Proponham um campo obrigatório e uma regra de validação para a ficha. Respondam quando prefeririam embutir dados e quando usariam uma referência ao lojista.

Respostas :
A filtragem feita busca diretamente o atributo do produto salvo, como ele é salvo em um JSON chave e valor a busca que referencia diretamente a propriedade do json apenas trará produtos cujo contenham a propriedade json solicitada, e a função gte significa greater than or equal 20 e como o produto salvo contem 25 no valor da chave foi buscado, enquanto livro não contem esta chave no json.

Campo proposto referencia com logista Loja_ID 
validação : Constraint de FK com logistas ativos

## T1 E6 Chave valor e expiração
```text
SET sessao:abc "cliente=42" EX 900
GET sessao:abc
```
Premissas: SET acontece em t igual a zero; não há nova escrita nem renovação. GET em t igual a 10 segundos retorna cliente=42. GET em t igual a 901 segundos retorna ausência de valor. Expliquem o significado de EX 900, calculem a duração em minutos e indiquem por que o único registro de um pedido não deveria usar essa política de expiração.
Distingam cache reconstruível e fonte oficial. Proponham como a aplicação identifica que a sessão expirou sem concluir que o cliente foi apagado do cadastro.

Resposta :
O significado de EX 900 é que o registro é temporario com base em 900 segundos, o que dá 15 minutos, ao tentar consultar após o periodo definido no set o registro já se apagou, logo para a consolidação de um pedido não é recomendado. 

É necessário distinguir a fonte oficial, que armazena os dados definitivos da operação, do cache reconstruível, utilizado para informações temporárias que podem ser recuperadas ou geradas novamente.

Uma aplicação pode utilizar o PostgreSQL como fonte oficial dos clientes e o Redis para armazenar sessões temporárias associadas ao identificador do cliente, com prazo de expiração. Quando uma consulta retornar ausência de valor para a sessão, a aplicação deve identificá-la como expirada ou inválida

## T1 E7 Caminhos em um grafo
Dados: clientes A e B usam dispositivo X; cliente C usa dispositivo Y; A realizou pedido 701. Existe exatamente uma relação USA por par cliente dispositivo no conjunto.
```text
MATCH (a:Cliente {id:'A'})-[:USA]->(d:Dispositivo)
      <-[:USA]-(outro:Cliente)
WHERE outro.id <> a.id
RETURN DISTINCT outro.id ORDER BY outro.id
```
Resultado fornecido: B. Descrevam o caminho percorrido e a função de DISTINCT. Expliquem por que a consulta não retorna C e por que compartilhar dispositivo não constitui, por si só, prova de fraude. Indiquem um custo de ampliar indiscriminadamente a profundidade de navegação.

Resposta :
Sobre o retorno mostrar B o motivo é que B é o unico usuario diferente de A que usa o mesmo dispositivo de A, O distinct serve pois há a possibilidade de B compartilhar mais de um dispositivo com A, não mostrou C pois C não compartilha dispositivo com A, apenas por este motivo não mostrou.

Compartilhar dispoitivos não comprova fraude pois uma mesma pessoa pode ser dona de dois celulares assim como permitir que outros entrem em suas contas no seu dispositivo, como um namorado e namorada ou um funcionario com celular empresarial e pessoal. 

Ampliar indiscriminadamente significa quase o mesmo que aplicar joins entre tabelas cujo as linhas se repetem, Como grafos podem ter muitas relações entre redes, se a pesquisa não for controlada há o risco de verificações redundantes, graças ao grande numero de ligações seria custoso em relação a memoria e processamento do sistema.
# Exercícios de interpretação temporal e vetorial
## T1 E8 Janela de tempo e agregação
A tabela medicao contém servico checkout e as observações em UTC de 22 setembro 2026: 10h00 com 90 ms; 10h01 com 110 ms; 10h02 com 210 ms; 10h03 com 120 ms. Não há outras linhas no intervalo.
```sql
SELECT AVG(latencia_ms) AS media_ms,
       MAX(latencia_ms) AS pico_ms
FROM medicao
WHERE servico = 'checkout'
  AND instante >= TIMESTAMPTZ '2026-09-22 10:00:00+00'
  AND instante < TIMESTAMPTZ '2026-09-22 10:04:00+00';
```
Resultados fornecidos: média 132.5 ms e pico 210 ms. Refaçam a conta, expliquem o limite superior exclusivo e indiquem uma informação que a média não preserva. Proponham retenção para detalhe recente e resumo histórico, explicitando que os prazos são decisões do grupo, não exigências universais.

Resposta :
AVG seria a media semelhante a um sum()/count(*), max não tem calculo, mas busca o maior valor da colunas retornado naquelas condições. 

O limite superior exclusivo, provavelmente foi para identificar alguma anomalia que ocorreu mais ou menos por aquele horario, algo situacional.

A media não preserva variação dos dados, Sendo assim, em situações com picos ela será muito afetada, sendo mais efetivo normalmente um P90+, Dependendo da quantidade de registros mediana pode salvar, e assim como qualquer função de agregação perde granularidade do dado linha a linha

detalhes recentes podem ser guardados em alta disponibilidade por 30 dias a 1 ano, depende da natureza do serviço, já o restante pode ser mostrado utilizando funções de agregações como de hora em hora, ou mesmo de dia a dia, enquanto os dados de granularidade menor podem ser salvos em um disco de menor disponibilidade para armazenamento historico.
## T1 E9 Similaridade por distância
Use os vetores didáticos q=(1;0), A=(0,9;0,1) e B=(0;1). O ponto e vírgula separa coordenadas; a vírgula representa decimal. Em uma tabela com coluna embedding do tipo vector, considere a ordenação por distância euclidiana do pgvector.
```sql
SELECT id, embedding <-> '[1,0]' AS distancia
FROM candidato
ORDER BY distancia, id
LIMIT 2;
```
Resultados fornecidos: A com aproximadamente 0.141421 e B com aproximadamente 1.414214. Mostrem o cálculo das duas distâncias, justifiquem a ordem e indiquem por que isso não representa probabilidade de o produto ser adequado. Os vetores foram fornecidos; não é necessário gerar embeddings ou instalar pgvector para esta interpretação.
Diferenciem uma busca exata da consulta apoiada por um índice aproximado. Citem uma regra de compra que deve continuar sendo verificada após recuperar candidatos.

Resposta :
O calculo que é feito na query é a distancia entre pontos em um plano cartesiano, nesse caso como esta ordenando por distancia e ID retorna A primeiro pois ele é mais proximo de Q do que B. 

Apenas proximidade pode não ser o necessario para atender a necessidade do cliente, estoque, valor entre outras caracteristicas podem ser importantes assim como se o produto se encontra ativo. 

A diferença entre uma busca exata é a verificação de todos os registros um a um em busca do mais aproximado matematicamente já o indice aproximado é uma verificação por aproximação inicial dos principais candidatos por meio de certas categorias, isso pode tanto auxiliar quanto atrapalhar caso as informações indexadas não contribuam corretamente com a consulta solicitada.

Uma regra de negocio seria produtos ativos com estoque disponivel. 
## T1 E10 Matriz de seleção
Preencham uma linha para cada família: relacional, documentos, chave valor, grafos, séries temporais e vetorial. Colunas obrigatórias: estrutura, operação da Loja Horizonte, consulta dominante, garantia necessária, crescimento esperado, custo operacional e limitação. Crescimento pode ser descrito por padrão de carga, sem inventar benchmark.
## T1 E11 Duas decisões arquiteturais
Produzam ADR 01 para pedidos e reservas e ADR 02 para catálogo e busca. Cada registro contém contexto, decisão, requisito atendido, alternativa rejeitada, compromisso assumido e condição concreta de revisão. Cada ADR deve ter entre 150 e 220 palavras. Combinar capacidades em um mesmo produto é permitido quando justificado.
