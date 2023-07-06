# IdralwelVersionRefactor

A program made in Java19 to easely translate projects / folders that have similarities and that needs mapping operations over their files

It was made for a specific needs for the project Idralwel | Tales of Aeshan

## Situation

Let's say you have a very huge program called `MyProject`, and it is in version 1.0.0
`MyProject` got a lot of bug and syntax error to correct, and so you spend time to correct everything.

Your version with all the corrections is called `MyProject_corrected`.

But the moment you're correction is complete, a new version of `MyProject` is uploaded, version 1.0.1.

This new version got some new functionnalities, but with a lot of the same bugs that you have to dealt with on version 1.0.0

Well this program is made to translate every file that can be replaced by your correction (`MyProject_corrected`) into a fresh new version of `MyProject` !

- All files that are identical between `MyProject`v1.0.0 and `MyProject`v1.0.1 will be replaced by `MyProject_corrected`'s version in the result project
- The new files of version `MyProject`v1.0.1 will be added to the result project
- The files deleted from `MyProject`v1.0.0 in version `MyProject`v1.0.1 will not be added to the result project
- All the custom files of `MyProject_corrected` will be added to the result project

## User Manual
The program has to be called with the following arguments:

**COMMAND**:
`./jarname SourcePath registerPath SolutionPath ResultPath` 
> Where jarname is the name of your jar (made out of the project files)


**ARGS REQUIRED**: `SourcePath` `RegisterPath` `SolutionPath` `ResultPath`

DESCRIPTION:
- `SourcePath` : The Path to the folder that contains the files that you want to translate (here `MyProjectV1.0.0`)
- `RegisterPath` : The Path to the folder that contains the files to be compared with (here `MyProjectV1.0.1`)
- `SolutionPath` : The Path to the folder that contains the files that are solutions if the recognition was successful (here `MyProject_corrected`)
- `ResultPath` : The Path to the folder that will contain the Output, it needs to be empty


## Output

The output is divided in 2 fields: 
- the console
- the log files

### The console 
- It displays a colorful logging of every files that could not been matched, with reasons.

- It also include statistics at the end to translate the operations done in number
![image](https://github.com/Ofghanirre/IdralwelVersionRefactor/assets/18609818/43470aeb-7066-40a5-9c8a-3e10b07c95b1)

### The log files

For each and every type of reason for 2 files not matching, there is a log_file to archive it !
> The logs are created in a folder called "logs"
![image](https://github.com/Ofghanirre/IdralwelVersionRefactor/assets/18609818/c457484f-be25-4b3b-98b4-836b5977f9dc)

