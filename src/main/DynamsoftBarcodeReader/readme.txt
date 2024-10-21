----------------------------------------
Dynamsoft Barcode Reader Java SDK Readme
----------------------------------------

Your use of Dynamsoft Barcode Reader Software is governed by the EULA located at license.txt. The software may contain third party software which requires notices and/ or additional terms and conditions in legal.txt.

Introduction
The Java SDK is developed by using Java Native Interface to call the C API of Dynamsoft Barcode Reader. The JAR file is also available on Dynamsoft server for developers to add dependencies easily via Maven repositories, as follows:

```xml
<repositories>
    <repository>
         <id>dbr </id>
         <url>https://download2.dynamsoft.com/maven/dbr/jar</url>
    </repository>
</repositories>
<dependencies>
    <dependency>
        <groupId>com.dynamsoft</groupId>
        <artifactId>dbr</artifactId>
        <version>9.6.40.1</version>
    </dependency>
</dependencies>                          
```

Supported platforms
- Windows x86/x64
- Linux x64/ARM64
- macOS x64/ARM64

Environment
- Java 1.7+

Directory structure after extraction:
./documents
./images
./lib
./samples

How to run the sample:
1. Open Eclipse and import the "/samples/BarcodeReaderDemo" project. 
2. Run as Java application and follow the instruction step by step.

How to extend your trial license:
1. Visit "https://www.dynamsoft.com/customer/license/trialLicense?product=dbr&utm_source=installer&package=java" to retrieve a trial license.
2. Find the string "initLicense" in the file "src\com\dynamsoft\demo\BarcodeReaderDemo.java" and replace it with the new key.

If you run into any issues, please feel free to contact us at support@dynamsoft.com.

Copyright © Dynamsoft Corporation.  All rights reserved.