set title "Insertions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.25*x**0)+(1.6944444444444449*x**1)+(-1.9444444444444449*x**2)
set xrange [0:1]
set yrange [0:1]
plot f(x)
