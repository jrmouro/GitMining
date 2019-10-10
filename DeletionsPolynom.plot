set title "Deletions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(0.0740640014504578*x**0)+(2.1131000945387663*x**1)+(-15.734546077216725*x**2)+(38.0735010137376*x**3)+(-38.46860866849987*x**4)+(13.942489635989766*x**5)
set xrange [0:1]
set yrange [0:1]
plot f(x)
