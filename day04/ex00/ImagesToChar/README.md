<pre>
.----. .-.   .-..----.    .----. .----. .-..-. .-. .---. .----..----. 
| {}  }|  `.'  || {}  }   | {}  }| {}  }| ||  `| |{_   _}| {_  | {}  }
| {}  }| |\ /| || .--'    | .--' | .-. \| || |\  |  | |  | {__ | .-. \
`----' `-' ` `-'`-'       `-'    `-' `-'`-'`-' `-'  `-'  `----'`-' `-'
<pre>

# First step you need to compile the projects

```
    $ javac -d target src/java/fr/leet/printer/*/*
```

# After that you need to run using this command

```
    #                                      white black example.bmp
    #                                          |     |      | 
    #                                          ----  ---    |
    #                                             |    |    |
    $ java -cp target fr.leet.printer.app.Program '*' '-' it.bmp
```
