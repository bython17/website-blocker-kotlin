## Website Blocker

This is a simple kotlin program that blocks the given sites by modifying the hosts file. Nothing fancy.
<br>

## Requirements
- OpenJDK 17

## Installation
1. First clone this repository to your machine and cd to the root
```bash
git clone https://github.com/bython17/website-blocker-kotlin.git && cd website-blocker-kotlin
```
2. Now build the program using [gradle](https://gradle.org). _This might take a while since gradle will download itself and other dependencies_
```bash
./gradlew build
```
3. Then execute the `jar` file found in `<PROJECT_ROOT>/build/libs/WebsiteBlocker-0.1.0.jar` using the `wb` script.
```bash
$ ./wb --help

Usage: wb options_list
Subcommands:
    list - List hosts file entries(websites) that are added using this program
    add - Add a hosts file entry(website) to the hosts file.
    remove - Remove a hosts file entry(website) from the hosts file.

Options:
    --redirect-ip, -r [0.0.0.0] -> The IP address the blocked will be redirected to(Tip: Spin up ur own server and put the IP here) { String }
    --help, -h -> Usage info
```
**NB**: If you are to add or remove a website then make sure you have the proper privileges
<br>

## NOTE
This is a project that was not built for usage in mind. Just use it if you want to read the code. It's purely built to practice kotlin.
