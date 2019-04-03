# compilers-2019
main repository for lab works

Short description about your chosen theme
====

Язык для имитации с помощью петри сети. 

Поддерживаются:
 - временные задержки в переходах, заданные с законом распроделения 
 - многоканальные переходы
 - приоритетность переходов
 - задание вероятности случайного выбора конфликтного перехода
 - дуги с заданной кратностью
 - информационные дуги

Description of your language (types, built-in functions etc.)
====

БНФ:

program = create_net simulate stats

create_net = ident '{' set_places set_transitions set_arcs '}'

ident = letter{letter|digit|'_'}

set_places = set_place {, set_place} 

set_place = ident ['(' int ')'] 

set_transitions = set_transition {, set_transition}

set_transition = ident [ '(' ['delay' '(' float ')'] ['deviation' '(' float ')'] ['distribution' '(' ('const' 'exp' 'norm' 'uniform') ')'] ['priority' '(' int ')'] ['probability' '(' float ')'] ')' ]

set_arcs = set_arc {, set_arc}

set_arcs = ident ['(' int ')']'->' ident [inf]

simulate = 'simulate' int

stats = stat {, stat}

stat = ident ('max'|'min'|'mean'|'value')

С помощью этого языка можно создать сеть петри, запусить ее и выдать определенные результаты на определенных элементах

Description 
===

#Лексический анализатор:

Разбивает текст программы на лексемы: 
PLACES,
TRANSITIONS, 
ARCS, 
IDENTIFIER,
INT,
FLOAT,
DELAY,
DEVIATION,
DISTRIBUTION,
PRIORITY,
PROBABILITY,
CONST,
EXP,
NORM,
UNIFORM,
CONNECTION, 
INF,
LEFT_BRACKET,
RIGHT_BRACKET,
LEFT_BRACE,
RIGHT_BRACE,
COMMA,
SIMULATE,
VALUE,
MIN,
MAX,
MEAN,
END_OF_INPUT

#Синтаксический анализатор:


Проверяет, чтобы лексемы находились в нужном порядке, создает объектную модель программы.

#Семантический анализатор:

Проверяет, что
 - не дублируются идентификаторы
 - не дублируются дуги
 - дуги идут только с позиции с переход или с перехода в позицию
 - информационные дуги только входят в переход
 - все идентификаторы, которые встречаются в статистике, заданы 
 - для переходов не спрашивается статистика "value"

Примеры кода
====
Примеры кода лежат в папке example

Запуск интерпритатора
====
Для запуска нужно вызвать скрипт petri/petri.sh:

petri.sh /path/to/source/file.ptr
