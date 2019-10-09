set title "Deletions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.038461538461538464*x**0)+(1.1153846153846156*x**1)+(-1.1538461538461542*x**2)
set xrange [0:1]
set yrange [0:1]
plot f(x)
