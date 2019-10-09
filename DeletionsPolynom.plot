set title "Deletions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.08849431818181819*x**0)+(-3.0034051452020205*x**1)+(26.201365402537277*x**2)+(-95.00844099476912*x**3)+(166.14510525643334*x**4)+(-139.0213519119769*x**5)+(44.598233074795566*x**6)
set xrange [0:1]
set yrange [0:1]
plot f(x)
