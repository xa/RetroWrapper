# RetroWrapper
Enables you to able to play _fixed_ old versions of minecraft without ever touching .jar files, even offline!

**WHAT IS DONE**
- Fixed indev loading
- Skins (with offline cache!)
- Sounds
- Saving
- Online Saving

**HOW TO USE** (dont worry I'll make easy installer in few days)

Download retrowrapper-1.0.jar from releases.

Navigate to .minecraft/libraries/com/

Create new folder 'zero' and navigate to it

Create new folder 'retrowrapper' inside 'zero' and navigate to it

Create new folder '1.0' inside 'retrowrapper' and navigate to it

Copy retrowrapper-1.0.jar to '1.0'

Now go into .minecraft/versions/

Copy that folder you want to patch and add -retro to its name (eg. c0.30_01 to c0.30_01-retro)

Go inside that folder and add -retro to all filenames inside it

Edit <version>.json and
  
- add -retro to id (eg. replace **"id": "c0.30_01c",* with *"id": "c0.30_01c-retro",**)
- replace **"libraries":** with **"libraries": [{"name": "net.minecraft:launchwrapper:1.6"},**
- replace **--tweakClass net.minecraft.launchwrapper....VanillaTweaker** with **--tweakClass com.zero.retrowrapper.RetroTweaker**
  
Launch Minecraft and choose newly created version!





Uses minimal-json by ralfstx
