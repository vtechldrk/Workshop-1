# TaskWorkshop
Console program for managing tasks.

More is comming...

## Features

This very simple java program allow you to manage your tasks list. You can list, add or remove tasks. All of the tasks are keep in the file called tasks.csv.
After program starts it is checking is there a file called tasks.csv in the working directory, if it is not, than you will have to decide wheter you want to program create it for you with some example tasks or not. File format is csv [task description, due date, importancy].
After that program will start and show you menu:

> add </br>
> remove</br>
> list</br>
> exit</br>

### adding a task
When you choose add, program will ask you few questions...
Description
Data
If it is important or no 

### removing a task
Program will ask you which task you want to remove. You have to provide the number.
If you dont know which number, you can use list feature to get it.
After provide the number, program will show you which data you want to remove and ask for the confirmation.


### Aditional dependecies which is used:

         <dependency>
                <groupId>org.apache.commons</groupId>
                <artifactId>commons-lang3</artifactId>
                <version>3.9</version>
            </dependency>
        <dependency>
            <groupId>commons-validator</groupId>
            <artifactId>commons-validator</artifactId>
            <version>1.3.1</version>
        </dependency>
        
### To be done...

- [X] Add task
- [X] Remove task
- [ ] Edit single task
- [X] Add date validation
- [X] Save to file feature
- [ ] Write docimentation here...

## Install

Just download it [**HERE**](https://github.com/vtechldrk/Workshop-1/), ~~create a folder~~ put it in **_any_ folder** and double click on it to run.

### Requirments

> `#ffffff` JAVA version 
