set title "Insertions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.11249628970020777*x**0)+(2.7280562269431368*x**1)+(-16.60438074507531*x**2)+(34.23761584380462*x**3)+(-30.38264380650845*x**4)+(9.908856191135799*x**5)
set xrange [0:1]
set yrange [0:1]
plot f(x)
