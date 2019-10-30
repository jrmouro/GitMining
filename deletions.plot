set title "low-pro-for-jqueryDeletions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(-49395.96348030244*x**0)+(270840.3677382741*x**1)+(-527765.9977364371*x**2)+(440695.1520229152*x**3)+(-134373.45085214195*x**4)
set xrange [0:1]
plot f(x)
