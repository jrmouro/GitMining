set title "Deletions"
set xlabel "elapse time"
set ylabel "volume"
set grid
f(x)=(263.41837182138266*x**0)+(-16547.4596717566*x**1)+(308143.30966970895*x**2)+(-2589164.0283133695*x**3)+(1.1867403462195795E7*x**4)+(-3.222697457100994E7*x**5)+(5.3210916886027254E7*x**6)+(-5.2373838630036786E7*x**7)+(2.8213779612989195E7*x**8)+(-6393981.850371506*x**9)
set xrange [0:1]
set yrange [0:1]
plot f(x)