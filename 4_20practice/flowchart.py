import matplotlib
matplotlib.use('Agg')
import matplotlib.pyplot as plt
import matplotlib.patches as mpatches
from matplotlib.patches import FancyArrowPatch, FancyBboxPatch
import matplotlib.patheffects as pe
import numpy as np

plt.rcParams['font.sans-serif'] = ['Microsoft YaHei', 'SimHei', 'SimSun', 'Arial Unicode MS', 'DejaVu Sans']
plt.rcParams['axes.unicode_minus'] = False

fig, ax = plt.subplots(figsize=(22, 32))
ax.set_xlim(0, 22)
ax.set_ylim(0, 32)
ax.axis('off')
fig.patch.set_facecolor('#FAFAFA')

# ── Color palette ──
C_RECT  = '#DBEAFE'   # blue-100
C_RECT_B= '#1D4ED8'   # blue-700 border
C_DIAM  = '#FEF9C3'   # yellow-100
C_DIAM_B= '#B45309'   # amber-700 border
C_TERM  = '#D1FAE5'   # green-100
C_TERM_B= '#065F46'   # green-900 border
C_SUB   = '#EDE9FE'   # purple-100
C_SUB_B = '#5B21B6'   # purple-700 border
C_TXT   = '#1E293B'
C_ARR   = '#475569'
C_LOOP  = '#F97316'   # orange arrow for loop-back

def rect(ax, x, y, w, h, label, fc=C_RECT, ec=C_RECT_B, fontsize=9, radius=0.25):
    box = FancyBboxPatch((x - w/2, y - h/2), w, h,
                         boxstyle=f"round,pad=0.05,rounding_size={radius}",
                         facecolor=fc, edgecolor=ec, linewidth=1.5, zorder=3)
    ax.add_patch(box)
    ax.text(x, y, label, ha='center', va='center', fontsize=fontsize,
            color=C_TXT, fontweight='bold', zorder=4,
            multialignment='center')

def diamond(ax, x, y, w, h, label, fontsize=8.5):
    dx, dy = w/2, h/2
    pts = np.array([[x, y+dy], [x+dx, y], [x, y-dy], [x-dx, y]])
    poly = plt.Polygon(pts, facecolor=C_DIAM, edgecolor=C_DIAM_B, linewidth=1.5, zorder=3)
    ax.add_patch(poly)
    ax.text(x, y, label, ha='center', va='center', fontsize=fontsize,
            color=C_TXT, fontweight='bold', zorder=4, multialignment='center')

def terminal(ax, x, y, w, h, label, fontsize=9.5):
    ell = mpatches.Ellipse((x, y), w, h,
                            facecolor=C_TERM, edgecolor=C_TERM_B, linewidth=1.8, zorder=3)
    ax.add_patch(ell)
    ax.text(x, y, label, ha='center', va='center', fontsize=fontsize,
            color=C_TXT, fontweight='bold', zorder=4)

def arrow(ax, x1, y1, x2, y2, label='', color=C_ARR, lw=1.5, style='->'):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle=style, color=color, lw=lw,
                                connectionstyle='arc3,rad=0'))
    if label:
        mx, my = (x1+x2)/2, (y1+y2)/2
        ax.text(mx+0.12, my, label, fontsize=8, color=color, fontweight='bold', zorder=5)

def arrow_curve(ax, x1,y1,x2,y2, rad=0.3, label='', color=C_ARR, lw=1.5):
    ax.annotate('', xy=(x2, y2), xytext=(x1, y1),
                arrowprops=dict(arrowstyle='->', color=color, lw=lw,
                                connectionstyle=f'arc3,rad={rad}'))
    if label:
        mx = (x1+x2)/2
        my = (y1+y2)/2
        ax.text(mx+0.15, my, label, fontsize=8, color=color, fontweight='bold', zorder=5)

# ══════════════════════════════════════════════
#  Section title helper
# ══════════════════════════════════════════════
def section_title(ax, x, y, title):
    ax.text(x, y, title, fontsize=11, color='#312E81',
            fontweight='bold', ha='center', va='center',
            bbox=dict(boxstyle='round,pad=0.3', facecolor='#C7D2FE', edgecolor='#4338CA', lw=1.5))

# ══════════════════════════════════════════════
#  PART 1 — Main Program Flow  (left column)
# ══════════════════════════════════════════════
section_title(ax, 5.5, 31.3, '主程序流程')

# nodes  (x, y)
M = {
    'start':    (5.5, 30.5),
    'loop':     (5.5, 29.2),
    'q1':       (5.5, 27.8),
    'query':    (2.8, 26.5),
    'out':      (2.8, 25.3),
    'q2':       (7.8, 26.8),
    'q3':       (7.8, 25.4),
    'decl':     (5.8, 24.1),
    'assign':   (9.8, 24.1),
    'create':   (5.8, 22.9),
    'eval_a':   (9.8, 22.9),
    'convert':  (9.8, 21.7),
    'update':   (9.8, 20.5),
    'eval_q':   (2.8, 24.1),
    'output':   (2.8, 22.9),
}

terminal(ax, *M['start'], 3.2, 0.6, '开始  main()')
rect(ax,    *M['loop'],   4.2, 0.85,
     'while(sc.hasNext())\n读取一行语句 stat', fontsize=8.5)
diamond(ax, *M['q1'],     3.6, 0.85, 'stat.endsWith\n("?")')
rect(ax,    *M['query'],  3.0, 0.65, '查询表达式', fontsize=8.5)
rect(ax,    *M['eval_q'], 3.0, 0.65, 'parseExpr()\n计算表达式', fontsize=8)
rect(ax,    *M['out'],    3.0, 0.65, '输出结果', fontsize=8.5)

diamond(ax, *M['q2'],     3.2, 0.85, 'stat.contains\n("=")')
diamond(ax, *M['q3'],     3.4, 0.85, '含类型关键字?\nint/double/string')
rect(ax,    *M['decl'],   3.0, 0.65, '变量声明语句\n检查重复声明', fontsize=8.5)
rect(ax,    *M['assign'], 3.0, 0.65, '变量赋值语句\n检查变量存在', fontsize=8.5)
rect(ax,    *M['create'], 3.0, 0.65, '创建 variable 对象\n存入 HashMap', fontsize=8.5)
rect(ax,    *M['eval_a'], 3.0, 0.65, 'parseExpr()\n计算右侧表达式', fontsize=8)
rect(ax,    *M['convert'],3.0, 0.65, '类型转换 / 校验', fontsize=8.5)
rect(ax,    *M['update'], 3.0, 0.65, '更新 HashMap', fontsize=8.5)

# arrows — main flow
arrow(ax, 5.5,30.2, 5.5,29.65)
arrow(ax, 5.5,28.77, 5.5,28.23)
# q1 YES → query
arrow(ax, 3.7,27.8, 3.3,26.83, 'YES')
# q1 NO  → q2
arrow(ax, 7.3,27.8, 7.8,27.23, 'NO')
# query → eval_q
arrow(ax, 2.8,26.17, 2.8,24.45)
# eval_q → out
arrow(ax, 2.8,23.77, 2.8,23.23)
# q2 YES → q3
arrow(ax, 7.8,26.37, 7.8,25.83, 'YES')
# q2 NO  → decl (no-assignment pure declaration — rare path)
arrow(ax, 6.2,26.8,  5.8,24.43, 'NO')
# q3 YES → decl
arrow(ax, 6.1,25.4, 6.2,24.43, 'YES')
# q3 NO  → assign
arrow(ax, 9.4,25.4, 9.8,24.43, 'NO')
# decl → create
arrow(ax, 5.8,23.77, 5.8,23.23)
# assign → eval_a
arrow(ax, 9.8,23.77, 9.8,23.23)
# eval_a → convert
arrow(ax, 9.8,22.57, 9.8,22.03)
# convert → update
arrow(ax, 9.8,21.37, 9.8,20.83)

# loop-back arrows  (create / update / output → loop)
ax.annotate('', xy=(3.4, 29.2), xytext=(2.8, 22.57),
            arrowprops=dict(arrowstyle='->', color=C_LOOP, lw=1.4,
                            connectionstyle='arc3,rad=-0.4'))
ax.annotate('', xy=(3.4, 29.2), xytext=(5.8, 22.57),
            arrowprops=dict(arrowstyle='->', color=C_LOOP, lw=1.4,
                            connectionstyle='arc3,rad=0.35'))
ax.annotate('', xy=(7.0, 29.2), xytext=(9.8, 20.17),
            arrowprops=dict(arrowstyle='->', color=C_LOOP, lw=1.4,
                            connectionstyle='arc3,rad=0.45'))
ax.text(1.5, 26.0, 'loop back', fontsize=7.5, color=C_LOOP, fontstyle='italic')

# ══════════════════════════════════════════════
#  PART 2 — Expression Parsing (right column)
# ══════════════════════════════════════════════
section_title(ax, 16.5, 31.3, '表达式解析（递归下降）')

EX = 16.5   # center x
nodes_e = {
    'pe':      (EX, 30.4),
    'pe_left': (EX, 29.2),
    'pe_loop': (EX, 28.0),
    'pe_op':   (EX, 26.8),
    'pt':      (EX, 25.5),
    'pt_left': (EX, 24.3),
    'pt_loop': (EX, 23.1),
    'pt_op':   (EX, 21.9),
    'pf':      (EX, 20.6),
    'pf_q':    (EX, 19.4),
    'paren':   (13.5, 18.0),
    'unary':   (16.5, 18.0),
    'numvar':  (19.5, 18.0),
}

rect(ax, *nodes_e['pe'],     6.5, 0.75,
     'parseExpr()  —  处理 + − （最低优先级）',
     fc='#DBEAFE', ec='#1D4ED8', fontsize=9)
rect(ax, *nodes_e['pe_left'],6.5, 0.75,
     'left = parseTerm()',
     fc='#EFF6FF', ec='#93C5FD', fontsize=9)
diamond(ax, *nodes_e['pe_loop'], 4.0, 0.85, '遇到 + 或 − ?')
rect(ax, *nodes_e['pe_op'],  6.5, 0.75,
     'op = 当前运算符\nright = parseTerm()\nleft = applyOp(op, left, right)',
     fc='#EFF6FF', ec='#93C5FD', fontsize=8.5)

rect(ax, *nodes_e['pt'],     6.5, 0.75,
     'parseTerm()  —  处理 × / % （中优先级）',
     fc='#F3E8FF', ec='#7C3AED', fontsize=9)
rect(ax, *nodes_e['pt_left'],6.5, 0.75,
     'left = parseFactor()',
     fc='#FAF5FF', ec='#C4B5FD', fontsize=9)
diamond(ax, *nodes_e['pt_loop'], 4.0, 0.85, '遇到 × / % ?')
rect(ax, *nodes_e['pt_op'],  6.5, 0.75,
     'op = 当前运算符\nright = parseFactor()\nleft = applyOp(op, left, right)',
     fc='#FAF5FF', ec='#C4B5FD', fontsize=8.5)

rect(ax, *nodes_e['pf'],     6.5, 0.75,
     'parseFactor()  —  处理原子项（最高优先级）',
     fc='#DCFCE7', ec='#16A34A', fontsize=9)
diamond(ax, *nodes_e['pf_q'], 4.2, 0.85, '当前 Token 类型?')

rect(ax, *nodes_e['paren'],  3.8, 1.0,
     "Token = '('\n递归 parseExpr()\n消费 ')'",
     fc='#FEF9C3', ec='#B45309', fontsize=8)
rect(ax, *nodes_e['unary'],  3.8, 1.0,
     "Token = '−'（一元）\n递归 parseFactor()\n取负值",
     fc='#FEF9C3', ec='#B45309', fontsize=8)
rect(ax, *nodes_e['numvar'], 3.8, 1.0,
     'Token = 数字/变量\nparseNumber()\ngetVariable() → HashMap',
     fc='#FEF9C3', ec='#B45309', fontsize=8)

# arrows — expression flow
arrow(ax, EX, 30.02, EX, 29.57)
arrow(ax, EX, 28.83, EX, 28.42)
# loop YES → op
arrow(ax, EX, 27.57, EX, 27.17, 'YES')
# op → loop (back)
ax.annotate('', xy=(EX+2.2, 28.0), xytext=(EX+2.2, 26.8),
            arrowprops=dict(arrowstyle='->', color='#7C3AED', lw=1.3,
                            connectionstyle='arc3,rad=-0.5'))
# loop NO → parseTerm
arrow(ax, EX, 27.57, EX, 25.87, 'NO')
arrow(ax, EX, 25.12, EX, 24.67)
arrow(ax, EX, 23.87, EX, 23.52)
# term loop YES → op
arrow(ax, EX, 22.67, EX, 22.27, 'YES')
ax.annotate('', xy=(EX+2.2, 23.1), xytext=(EX+2.2, 21.9),
            arrowprops=dict(arrowstyle='->', color='#7C3AED', lw=1.3,
                            connectionstyle='arc3,rad=-0.5'))
# term loop NO → parseFactor
arrow(ax, EX, 22.67, EX, 20.97, 'NO')
arrow(ax, EX, 20.22, EX, 19.82)
# pf_q branches
arrow(ax, 14.3, 19.4, 13.5, 18.52, "'('")
arrow(ax, EX,   18.97, EX,  18.52, "'−'")
arrow(ax, 18.7, 19.4, 19.5, 18.52, '数字/变量')

# ── dashed call arrows from main to parseExpr ──
ax.annotate('', xy=(13.2, 30.0), xytext=(11.0, 22.9),
            arrowprops=dict(arrowstyle='->', color='#94A3B8', lw=1.2,
                            linestyle='dashed',
                            connectionstyle='arc3,rad=-0.25'))
ax.text(11.5, 26.8, '调用', fontsize=8, color='#94A3B8', fontstyle='italic')

# ══════════════════════════════════════════════
#  Legend
# ══════════════════════════════════════════════
lx, ly = 0.3, 3.5
ax.text(lx, ly+0.8, '图例', fontsize=9, fontweight='bold', color='#1E293B')
rect(ax,  lx+1.1, ly+0.35, 1.8, 0.42, '处理步骤', fc=C_RECT, ec=C_RECT_B, fontsize=8, radius=0.12)
diamond(ax,lx+3.5, ly+0.35, 1.8, 0.6,  '判断条件', fontsize=8)
terminal(ax,lx+5.8, ly+0.35, 1.8, 0.45, '开始/结束', fontsize=8)
ax.annotate('', xy=(lx+8.2, ly+0.35), xytext=(lx+7.2, ly+0.35),
            arrowprops=dict(arrowstyle='->', color=C_LOOP, lw=1.4))
ax.text(lx+8.3, ly+0.32, '回到循环', fontsize=8, color=C_LOOP)
ax.annotate('', xy=(lx+10.5, ly+0.35), xytext=(lx+9.5, ly+0.35),
            arrowprops=dict(arrowstyle='->', color='#94A3B8', lw=1.2, linestyle='dashed'))
ax.text(lx+10.6, ly+0.32, '调用关系', fontsize=8, color='#94A3B8')

# divider line between two sections
ax.plot([11.5, 11.5], [1.5, 31.8], color='#CBD5E1', lw=1.2, linestyle='--', zorder=1)

plt.tight_layout(pad=0.5)
plt.savefig('d:\\JavaCode\\4_20practice\\flowchart.png', dpi=160, bbox_inches='tight',
            facecolor='#FAFAFA')
print("done")