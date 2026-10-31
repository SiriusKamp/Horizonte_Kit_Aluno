# T2 E4 Quorum e cenário de partição
Esta atividade é uma simulação em conjunto fixo de três réplicas A B C, sem réplicas substitutas. W é número de confirmações de escrita e R é número de respostas de leitura. Não associem esses números automaticamente à configuração de PostgreSQL usada nas páginas anteriores.
## Casos obrigatórios
Para cada par W e R igual a 1 e 1, 2 e 2, 3 e 1, listem os conjuntos mínimos possíveis e respondam se toda leitura intersecta toda escrita. Mostrem um exemplo de interseção ou um contraexemplo com grupos disjuntos.
Considerando uma réplica inacessível, indiquem se restam participantes suficientes para cada limiar. Essa conta não inclui eleição de líder ou outras condições de protocolo. Mostrem por que R mais W maior que N é uma propriedade de interseção, e não uma prova completa de linearizabilidade.
## Partição dois mais um
Estado inicial x igual a zero em A B C. A e B podem conversar; C não troca mensagens com eles, mas continua acessível a um cliente. Em uma política que exige dois participantes para a operação, analisem quais lados atingem o limiar. Não afirmem que C está fisicamente desligado.
Histórico obrigatório: uma escrita x igual a um é confirmada em A B. Depois dessa confirmação, outro cliente inicia leitura em C. Respondam o que acontece se C devolve zero, espera por comunicação ou responde erro sem executar a leitura solicitada. Expliquem a tensão entre linearizabilidade e disponibilidade no modelo CAP.
Não é possível deduzir o valor correto apenas do silêncio. Descrevam outro histórico indistinguível para C em que a escrita em A não ocorreu. Separem o C de CAP das restrições de integridade do ACID.

Resposta: 
Considerando três réplicas (A, B e C)

W=1 e R=1: qualquer réplica pode realizar a operação. Não existe garantia de interseção, pois uma escrita em A pode ser seguida por uma leitura em C.
W=2 e R=2: os conjuntos possíveis são AB, AC e BC. Qualquer par intersecta outro par, garantindo pelo menos uma réplica em comum.
W=3 e R=1: a escrita exige todas as réplicas, enquanto a leitura exige apenas uma. Portanto, toda leitura intersecta a escrita.

A propriedade R+W>N garante a interseção entre os conjuntos de leitura e escrita, mas não comprova, isoladamente, a linearizabilidade, pois também são necessárias garantias de ordenação e tratamento das atualizações.

Com uma réplica inacessível, restam duas participantes. Assim, os limiares W=1, W=2, R=1 e R=2 ainda podem ser atingidos, enquanto W=3 não pode ser satisfeito.

Na partição dois mais um, A e B conseguem atingir o limiar de dois participantes, mas C, embora continue acessível ao cliente, não consegue. Após uma escrita x=1 ser confirmada em A e B, uma leitura iniciada em C não pode retornar zero sem violar a linearizabilidade. C pode aguardar a comunicação ou recusar a operação, sacrificando a disponibilidade. O silêncio não permite deduzir se a escrita ocorreu, pois C também poderia observar o mesmo estado caso nenhuma escrita tivesse sido realizada.

O C do CAP representa consistência no comportamento distribuído, enquanto a consistência do ACID trata das regras de integridade das transações.

## T2 E5 Garantias de sessão e BASE
Histórico H1 contém leituras da mesma sessão e do mesmo objeto, em ordem: versão 8 e depois 7. Histórico H2 contém uma escrita própria confirmada de versão 9 e, como primeira leitura posterior da sessão, versão 8. Para cada histórico, nomeiem a garantia violada e indiquem quais operações são comparadas.
Proponham um mecanismo para evitar H1, como roteamento com controle de progresso ou token de versão. Expliquem a condição em que fixar a sessão em uma réplica deixa de ser suficiente. Para H2, descrevam como assegurar que a leitura reflita a própria escrita.
Por fim, expliquem BASE sem tratá-lo como protocolo único. Informem as condições necessárias à convergência eventual e por que eventual não define um prazo máximo nem uma sequência monotônica para cada sessão.

Resposta :
No histórico H1, a consulta da versão legada viola a garantia de Monotonic Reads, pois uma leitura posterior não deve apresentar uma versão anterior à já observada.

Uma solução é utilizar tokens de versão ou roteamento com controle de progresso, exigindo que a réplica consultada tenha aplicado pelo menos a versão anteriormente observada. Fixar a sessão em uma réplica pode não ser suficiente caso ela falhe ou seja substituída por outra réplica atrasada.

No histórico H2, o cliente escreve a versão 9, recebe a confirmação e depois lê a versão 8. Isso viola a garantia de Read Your Writes, pois a sessão não consegue enxergar sua própria escrita confirmada. Uma solução é utilizar um token da versão confirmada e exigir que a leitura seja atendida por uma réplica que já tenha aplicado essa versão.

BASE representa os princípios Basically Available, Soft State e Eventual Consistency. A consistência eventual pressupõe que as atualizações possam ser propagadas, que a comunicação seja restabelecida e que exista um mecanismo de resolução de conflitos. Se novas atualizações cessarem e a propagação ocorrer, as réplicas poderão convergir. Entretanto, o conceito não estabelece um prazo máximo para essa convergência nem garante que cada sessão observe versões em ordem monotônica.