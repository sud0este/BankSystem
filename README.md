# FlowerBank
Plugin de banco para servidores Spigot/Paper, integrando Vault para saldo, Citizens para NPCs e ProtocolLib. Cada jogador ganha uma conta bancária com menus GUI para depositar, sacar, acompanhar histórico e evoluir níveis de conta com rendimento automático.

**Funcionalidades**
- NPC “Banqueiro” criado pelo comando `/bank set` abre o menu principal.
- Contas criadas no primeiro login; remoção segura ao sair do servidor.
- Depósitos e saques via GUI, com atalho “tudo / metade / 20%” ou valor digitado no chat.
- Histórico das últimas 10 transações exibido no menu.
- Rendimentos automáticos a cada 36h, limitados por nível de conta (projeção e tempo restante no menu).
- Níveis de conta configuráveis (padrão → ultra), cada um com custo, limite de saldo e teto de rendimento.
- Autosave periódico das contas (`autosave` em segundos) e rendimento contabilizado em loop de 1s.
- Integração com MySQL para persistência (estrutura básica pronta; rotinas de SELECT/UPDATE marcadas como TODO).

**Dependências de servidor**
- Spigot/Paper 1.21.8+.
- Vault.
- Citizens 2.0.33-SNAPSHOT+.
- ProtocolLib 5.1.0.

**Requisitos de build**
- Java 17.
- Maven 3.9+.
- Ajuste o caminho do ProtocolLib no `pom.xml` (`protocollib.path`, padrão `D:\API\ProtocolLib.jar`) ou instale o jar no repositório local.

**Como instalar (produção)**
1. Compile ou obtenha o jar gerado em `target/FlowerBank-1.0.jar`.
2. Coloque o jar em `plugins/` e garanta que Vault, Citizens e ProtocolLib estejam instalados.
3. Inicie o servidor para gerar `config.yml`, depois ajuste credenciais MySQL e parâmetros.
4. Reinicie o servidor e use `/bank set` (perm: `server.createnpc.bank`) para spawnar o banqueiro.
5. Clique no NPC para abrir o menu e testar depósitos/saques.

**Configuração (config.yml)**
- `autosave`: intervalo em segundos para salvar todas as contas (default 600 = 10 min).
- `yield_time`: tempo do ciclo de rendimento em segundos (default 129600 = 36h).
- `database.*`: host, port, user, password, name para MySQL.
- `bank_account_levels`: define nome, preço, limite de saldo, rendimento máximo e porcentagem de rendimento por nível (padrão → ultra já preenchidos).

**Comando e permissão**
- `/bank set` — cria o NPC Banqueiro. Permissão: `server.createnpc.bank`.

**Fluxo de jogo**
- Jogador entra → conta é criada no nível inicial.
- Ao clicar no Banqueiro → menu principal mostra saldo, projeção de rendimento e histórico.
- Depósitos/saques imediatos via GUI ou valor digitado no chat.
- Rendimentos caem automaticamente a cada ciclo; upgrading de nível aumenta limites e teto de rendimento.

**Limitações / TODO**
- Rotinas de persistência MySQL estão parcialmente marcadas como TODO; implementar SELECT/UPDATE antes de usar em produção.
- Textos de mensagens usam formatação de cores (§) e podem precisar de ajuste de encoding UTF-8 em seu ambiente.
