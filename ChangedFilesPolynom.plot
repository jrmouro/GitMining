set title "ChangedFiles"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.16666666666666666*x**0)+(2.611111111111112*x**1)+(-2.7777777777777786*x**2)
set xrange [0:1]
set yrange [0:1]
plot f(x)
