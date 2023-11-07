<pre>
 ______      __    __     _____       _____    ______      _____      __      _   ________    _____   ______    
(_   _ \     \ \  / /    (  __ \     (  __ \  (   __ \    (_   _)    /  \    / ) (___  ___)  / ___/  (   __ \   
  ) (_) )    () \/ ()     ) )_) )     ) )_) )  ) (__) )     | |     / /\ \  / /      ) )    ( (__     ) (__) )  
  \   _/     / _  _ \    (  ___/     (  ___/  (    __/      | |     ) ) ) ) ) )     ( (      ) __)   (    __/   
  /  _ \    / / \/ \ \    ) )         ) )      ) \ \  _     | |    ( ( ( ( ( (       ) )    ( (       ) \ \  _  
 _) (_) )  /_/      \_\  ( (         ( (      ( ( \ \_))   _| |__  / /  \ \/ /      ( (      \ \___  ( ( \ \_)) 
(______/  (/          \) /__\        /__\      )_) \__/   /_____( (_/    \__/       /__\      \____\  )_) \__/      
</pre>

# First step need to compile the projects

```
    $ javac -d target src/java/fr/leet/printer/*/*
```

# Copy the resources file into target
```
	$ cp -r src/resources target
```

# cd into target
```
	$ cd target
```

# Create a jar file 
```
	$ jar cvfm images-to-chars-printer.jar ../src/manifest.txt fr/leet/printer/*/*.class resources/image.bmp
```

# After that you need to run the jar using using this command
```
    # arg1 == black arg2 == white
	$ java -jar images-to-chars-printer.jar x y
```
