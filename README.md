# MyAirplaneFork

A template project for forking Airplane!

## Initial Setup

Fork this repository to your own repo, then clone it locally. Once you've cloned it, you can setup the patches:
```bash
cd MyAirplaneFork
./gradlew applyPatches
# or on windows (STRONGLY DISCOURAGED - see below)
gradlew.bat applyPatches
```

This will set up the entire project, as once it's done everything will be ready to go!

### Windows
Using Windows? Click [here](#step-by-step-guide-for-windows-using-wsl) to view the Windows Guide.

## Working on the fork

Once it applies the patches, you'll have two directories: `MyAirplaneFork-API`, and `MyAirplaneFork-Server`.
Inside the `MyAirplaneFork-API` you should be somewhat familiar seeing the Bukkit library.
Any interfaces to the server you want to expose to plugins, should go in this project.

Inside of `MyAirplaneFork-Server`, you'll find the actual implementation of the API.
Here is where you can make logic changes to the server, with the power to change anything in the implementation that you want!

## What is a patch file?

In order to not distribute any of Mojang's assets, a `.patch` file is basically the exact same thing as a Git commit.
It represents a change to the server, and applies on top of previous changes, which includes Mojang's original Minecraft server code.
(and CraftBukkit, and Spigot, and Paper, and Tuinity, and Airplane..)

Each patch represents 1 commit inside the respective project. 
`patches/api` adds commits to `MyAirplaneFork-API`, while `patches/server` adds commits to `MyAirplaneFork-Server`.
Both of `MyAirplaneFork-API` and `MyAirplaneFork-Server` have their own git repository inside them, however there's no remote (like GitHub) that you push these repositories to.
Instead, they get built from all the patches you have in your `patches` folder.

So if you want to create a new patch, just add a commit to either the API or Server folder, and all you need to do to generate the patch is:

```bash
./gradlew rebuildPatches
# or on windows
gradlew.bat rebuildPatches
```

This command takes your commits, and turns them back into the actual patch files that you push.
You should make sure that your fork never has the API and Server folders pushed, and just the `patches/api` and `patches/server` pushed.

## How do I update the upstream?

This step is fairly easy, assuming there's no merge errors. To update the upstream (Airplane), all you have to do is run:

```bash
./gradlew updateUpstream
# or on windows
gradlew.bat updateUpstream
```

Then to rebuild the API & Server folders with your patches, just reapply them:

```bash
./gradlew applyPatches
# or on windows
gradlew.bat applyPatches
```

You may at times run into an merge conflict when applying patches, this happens when 2 patches modify similar areas of code, and the system can't figure out how to make them work together.
You'll see in your terminal what files had a conflict, once you solve the files go into the project (either the API or Server) and run the following:

```bash
# this will add your fixed changes
git add --all
# this will continue the process of applying the patches
git am --continue
```

Once all your patches apply successfully, all you have to do is make sure you're in the main folder and run:

```bash
./gradlew rebuildPatches
# or on windows
gradlew.bat rebuildPatches
```

Finally, you have two options for making the commit:

```bash
git add --all
git commit -m 'Description of my changes'
```

Or if you want a fancy commit message that lists the upstream changes, you can just run

```bash
git add --all

./gradlew upstreamCommit
# or on windows
gradlew.bat upstreamCommit
```

With that, you've successfully updated your fork!

## Building the fork

If you're just building the fork to test, you can run:

```bash
./gradlew build
# or on windows
gradlew.bat build
```

Which will output your final JAR at `MyAirplaneFork-Server/build/libs/myairplanefork-server-1.16.5-R0.1-SNAPSHOT.jar`.
However, you should not distribute this JAR outside testing because it contains Mojang's copyrighted Minecraft code.
Instead, you can use the following command to generate a JAR that downloads Mojang's server when it's ran, and applies your changes on top:

```bash
./gradlew paperclip
# or on windows
gradle.bat paperclip
```

This will output your distributable JAR at `launcher-myairplanefork.jar`, right in your main directory!

# Step-by-Step Guide for Windows using WSL
This is a step-by-step guide on how to fork Airplane on Windows with WSL. You should still read the above information to get to grips with how to patch the plugin, however.

## 1. Setting up WSL
This guide will require you to use Windows Subsystem for Linux (or, just Linux). We'll be using the Debian distribution, but if you are more comfortable using a different distribution, such as Ubuntu or CentOS, this guide should still work (on CentOS, replace apt commands with yum).

### Enable WSL
Enable Windows Subsystem for Linux by going to **Turn Windows features on or off** in the Control Panel (or search for it). Select the [x] Checkbox beside `Windows Subsystem for Linux`, and click OK. You will likely need to restart your computer to fully enable this feature.
### Store Package
Go to the Windows Store and get the **Debian** package, or click [here](https://www.microsoft.com/en-us/p/debian/9msvkqc78pk6#activetab=pivot:overviewtab).
### Open Debian
From the start menu, type 'debian' to open the Debian console, or type 'debian' in Command Manager.

## 2. (WSL) Install & Setup Dependencies
After setting up Debian, run the following commands:

### Install Dependencies
```bash
$ apt update
$ apt install -y curl git default-jdk
```
### Configure Git
If you use GitHub/GitLab you should make your user.email the primary email of that account. 
```bash
$ git config --global user.name "YOUR NAME"
$ git config --global user.email your@email.com
```

## 3. Cloning MyAirplaneFork
You should now fork this repository. Ensure you run this inside WSL, not any other terminal.
```bash
$ cd ~ # Optional, takes you to your home directory
$ git clone https://github.com/TECHNOVE/MyAirplaneFork.git my-fork-directory
```

Once the repository has finished downloading, enter it.
```bash
$ cd my-fork-directory
```

## 3. Apply the patches
Apply the default patches. This will download the Minecraft server, decompile and de-obfuscate the code using the mappings (automatically provided), then apply the various patches from Airplane, Tuinity, Paper, Spigot and Bukkit/CraftBukkit.

This is quite an intensive operation, and can take several minutes (up to 30 in some cases) to fully apply.

```bash
$ ./gradlew applyPatches
```

## 4. Done!
You should now have a complete version of Airplane, forked and ready to go on your codebase.

### Patching with IntelliJ IDEA
If using IntelliJ IDEA, you should be running at least **2021.1** to have full Windows Subsystem for Linux support. If you followed the guide verbatim, your code will be at `\\wsl$\Debian\home\<username>\my-fork-directory`.

IntelliJ IDEA 2021.1 added support natively for WSL projects - without this version, the Gradle sidebar and potential indexing may fail.


## Questions?

If you need help with any of this or run into issues, feel free to ask questions in the Airplane Discord located here: https://discord.gg/3gtc45q


## Additional Info

### Using an IDE

I personally recommend IntelliJ as my IDE of choice, but there's one thing you have to make sure not to do.
Our build system is [Toothpick](https://github.com/jpenilla/Toothpick), put together by the amazing [Purpur](https://github.com/pl3xgaming/Purpur) team.
Unlike other forks, this system uses Gradle instead of Maven. 
Inside the Server folder however, you will still find a `pom.xml` that IntelliJ may try to import. If it does, make sure to unlink the Maven project and doublecheck that the Gradle project is imported

### Changing Branding

This won't be a comprehensive guide, but if you need to change branding you should go to these places:

- build.gradle.kts (forkName, groupId, forkUrl, paperclipName)
- settings.gradle.kts (forkName)
- MyAirplaneFork-Server/pom.xml (API dependency)
- PaperVersionFinder.java (change GitHub repo)
- MinecraftServer.java (getServerModName)
- CraftServer.java (serverName)
- Versioning.java (path to pom.properties should match API)

### Licensing

This repository is licensed under MIT, however I'm fairly convinced due to the GPL licensing of upstreams that forks need to be licensed GPL as well.

