# ADR 001: Adoção do Estilo Arquitetural Monolítico  
Situação: aceita

## Contexto

O sistema StockFlow precisa controlar entradas e saídas de estoque com alta integridade de dados, garantindo que o saldo esteja sempre consistente em tempo real para evitar vendas de produtos inexistentes. Além disso, há demanda por auditabilidade detalhada, integração desacoplada entre módulos e monitoramento robusto para garantir rápida detecção e resolução de problemas.

## Opções Avaliadas

- Arquitetura de microsserviços: oferece alta escalabilidade e independência de deploy por serviço, porém adiciona complexidade operacional, orquestração, observabilidade sofisticada e desafios de consistência distribuída.  
- Monolito: única unidade de deploy que facilita transações ACID locais, agora complementado com broker de eventos interno para comunicação assíncrona, event sourcing para persistência baseada em eventos e observability para monitoramento estruturado.

O contexto atual demanda priorizar consistência de dados e time-to-market, aceitando abrir mão de uma escalabilidade extrema imediata.

## Decisão

Será adotado o Estilo Arquitetural Monolítico para o backend, exposto por meio de uma API REST. A aplicação será uma única unidade de deploy, incorporando:  
- Broker de eventos para publicar e consumir eventos internamente, desacoplando módulos e facilitando integrações.  
- Event sourcing para armazenar o estado do sistema baseado em um log imutável de eventos, garantindo auditabilidade e reconstrução de estado.  
- Observability com métricas, logs estruturados e tracing para monitoramento e diagnóstico eficientes.

## Justificativa

A complexidade da coordenação de múltiplos serviços distribuídos não justifica o custo operacional para o MVP, mas as vantagens do event-driven architecture serão aproveitadas internamente no monolito para modularização desacoplada e auditabilidade avançada. O event sourcing reforça a integridade e transparência do estado crítico do estoque. Observability assegura insights de performance e saúde do sistema, facilitando respostas rápidas a incidentes.

## Consequências

Positivas:  
- Comunicação interna desacoplada e flexível via eventos, facilitando a evolução do código.  
- Histórico completo das ações via event sourcing, auxiliando auditoria, debugging e futuras evoluções.  
- Monitoramento detalhado e análise da execução, antecipando falhas e melhorando a manutenção.  
- Pipeline de deploy único simplificado, sem a complexidade de microsserviços distribuídos.

Negativas:  
- Configuração e operação do broker de eventos e do event sourcing aumentam complexidade da infraestrutura e curva de aprendizado.  
- Risco de sobrecarga do monolito pela mistura de paradigmas assíncronos e síncronos, requer disciplina no design.  
- Escalabilidade limitada à aplicação inteira, exigindo escala vertical ou replicação completa.

Trade-off: a arquitetura monolítica conserva simplicidade no deploy e consistência com transações locais, enquanto adota benefícios do event-driven e monitoramento avançado para mitigar riscos futuros e garantir robustez.

## Conformidade Arquitetural

Serão utilizados testes automatizados de arquitetura e ferramentas específicas para garantir que as regras de uso do broker de eventos, event sourcing e observability sejam respeitadas e mantidas coerentes ao longo do desenvolvimento e integração contínua.
