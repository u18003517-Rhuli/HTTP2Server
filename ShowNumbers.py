#!/usr/bin/env python
# -*- coding: utf-8 -*-

print("<!Doctype html>\n")
print("<html lang='en' dir='ltr'>\n")
print("\t<head>\n")
print("\t\t<meta charset ='utf-8'>\n")
print("\t\t<title> Calcalate My Existance </title>\n")

print("\t\t<style>\n")
print("\t\t\tbody{\n")
print("\t\t\t\tbackground-color: #a9bd7e;\n")
print("\t\t\t}\n")

print("\t\t\ttable{\n")
print("\t\t\t\tmargin: auto;\n")
print("\t\t\t\tbackground-color: grey;\n")
print("\t\t\t\twidth: 65%;\n")
print("\t\t\t\theight: 200px;\n")
print("\t\t\t\ttext-align: center;\n")
print("\t\t\t\tborder-radius: 4px;\n")
print("\t\t\t\tmargin-top:125px;\n")
print("\t\t\t}\n")
print("\t\t\ttable input{n")
print("\t\t\t\twidth: 15%;\n")
print("\t\t\t\theight: 40%;\n")
print("\t\t\t}\n")
print("\t\t\ttable input:hover{\n")
print("\t\t\t\twidth: 15%;\n")
print("\t\t\t\theight: 40%;\n")
print("\t\t\t}\n")

print("\t\t\t#calculator{\n")
print("\t\t\t\tposition: absolute;\n")
print("\t\t\t\tmax-width: 80%;\n")
print("\t\t\t\tleft: 58%;\n")
print("\t\t\t\ttop: 10%;\n")
print("\t\t\t}\n")

print("\t\t\tinput{\n")
print("\t\t\t\toutline: 0;\n")
print("\t\t\t\tposition: relative;\n")
print("\t\t\t\tleft: 5px;\n")
print("\t\t\t\ttop: 5px;\n")
print("\t\t\t\tborder: 0;\n")
print("\t\t\t\tcolor: #495069;\n")
print("\t\t\t\t//background-color: #658696;\n")
print("\t\t\t\twidth: 20%;\n")
print("\t\t\t\theight: 15%;\n")
print("\t\t\t\tfloat: left;\n")
print("\t\t\t\tmargin: 5px;\n")
print("\t\t\t\tfont-size: 20px;\n")
print("\t\t\t\tbox-shadow: 0 4px rgba(0,0,0,0.2);\n")
print("\t\t\t}\n")

print("\t\t\tinput:hover{\n")
print("\t\t\t\tborder: 0 solid #000;\n")
print("\t\t\t\tcolor: #495069;\n")
print("\t\t\t\tbackground-color: #8f5fda;\n")
print("\t\t\t\tborder-radius: 4px;\n")
print("\t\t\t\twidth: 20%;\n")
print("\t\t\t\theight: 15%;\n")
print("\t\t\t\tfloat: left;\n")
print("\t\t\t\tmargin: 5px;\n")
print("\t\t\t\tfont-size: 20px;\n")
print("\t\t\t\tbox-shadow: 0 4px #644294;\n")
print("\t\t\t}\n")
print("\t\t\t#Answer{\n")
print("\t\t\t\twidth: 90%;\n")
print("\t\t\t\tfont-size: 16px;\n")
print("\t\t\t}\n")

print("\t\t\t#Values{\n")
print("\t\t\t\twidth: 90%;\n")
print("\t\t\t\tfont-size: 16px;\n")
print("\t\t\t}\n")
print("\t\t\t#answer{\n")
print("\t\t\t\twidth: 270px;\n")
print("\t\t\t\tfont-size: 26px;\n")
print("\t\t\t\ttext-align: center;\n")
print("\t\t\t\tbackground-color: #f1faeb;\n")
print("\t\t\t\tfloat: left;\n")
print("\t\t\t}\n")

print("\t\t\t#answer:hover{\n")
print("\t\t\t\twidth: 270px;\n")
print("\t\t\t\tfont-size: 26px;\n")
print("\t\t\t\ttext-align: center;\n")
print("\t\t\t\tbox-shadow: 0 4px rgba(0,0,0,0.2);\n")
print("\t\t\t\tbackground-color: #f1faeb;\n")
print("\t\t\t}\n")

print("\t\t\t.operator{\n")
print("\t\t\t\tbackground-color: #f1ff92;\n")
print("\t\t\t\tposition: relative;\n")
print("}\n")


print("\t\t\t.operator:hover{\n")
print("\t\t\t\tbackground-color: #e7f56b;\n")
print("\t\t\t\tbox-shadow: 0 4px #b7c259;\n")
print("\t\t\t}\n")
print("\t\t\t#clear{\n")
print("\t\t\t\tfloat: left;\n")
print("\t\t\t\tposition: relative;\n")
print("\t\t\t\tdisplay: block;\n")
print("\t\t\t\tbackground-color: #ff9fa8;\n")
print("\t\t\t\tmargin-bottom: 15px;\n")
print("\t\t\t}\n")

print("\t\t\t#clear:hover{\n")
print("\t\t\t\tfloat: left;\n")
print("\t\t\t\tdisplay: block;\n")
print("\t\t\t\tbackground-color: #f297a0;\n")
print("\t\t\t\tmargin-bottom: 15px;\n")
print("\t\t\t\tbox-shadow: 0 4px #cc7f86;\n")
print("\t\t\t}\n")
print("\t\t</style>\n")
print("\t</head>\n")

print("\t<body>\n")
print("\t\t<h2> Picture </h2>\n")

print("\t\t<form action='ShowNumbers.py' method='get'>\n")
print("\t\t\t<input type='submit' value='Show Numbers' >\n")
print("\t\t</form>\n")

print("\t\t<form action='AddNumbers.py' method='get'>\n")
print("\t\t\t<input type='submit' value='Add Numbers' >\n")
print("\t\t</form>\n")

print("\t\t<form action='DeleteNumbers.py' method='get'>\n")
print("\t\t\t<input type='submit' value='Delete Numbers' >\n")
print("\t\t</form>\n")

print("\t\t<form action='UpdateNumbers.py' method='get'>\n")
print("\t\t\t<input type='submit' value='Update Numbers' >\n")
print("\t\t</form>\n")

print("\t\t<form action='LookNumbers.py' method='get'>\n")
print("\t\t\t<input type='submit' value='Search Numbers' >\n")
print("\t\t</form>\n")





print("\t\t<form>\n")
print("\t\t\t<table>\n")

file = open("ServerData/Cellphones.txt", "r")
Lines = file.readlines()


for line in Lines:
    userData = line.split(",")
    print("\t\t\t\t<tr>\n")
    print("\t\t\t\t\t<td>\n")

    cell = userData[0] + " " + userData[1]
    print("\t\t\t\t\t\t<input type='text' id ='Values' size='16' disabled=true value ='" + str(cell) + "' >\n")

    if(userData[2] != "null"):
        print("\t\t\t\t\t\t<img alt='Profile' width='100' height='100' src= 'ServerData/" + str(userData[2]) + "' >\n")
    else:
        print("\t\t\t\t\t\t<img alt='Profile' width='100' height='100' src= 'ServerData/DEFAULT.png' >\n")

    print("\t\t\t\t\t</td>\n")
    print("\t\t\t\t</tr>\n")


file.close()

print("\t\t\t</table>\n")
print("\t\t</form>\n")


print("\t\t<form action='index.py' method='post' enctype='multipart/form-data'>\n")
print("\t\t\t<input type='text' id='calValue' hidden name='calValue' >\n")
print("\t\t\t<input type='submit' value='Home' >\n")
print("\t\t</form>\n")



print("\t</body>\n")

print("</html>\n")
