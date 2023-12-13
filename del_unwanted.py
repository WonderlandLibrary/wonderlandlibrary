import os
from colorama import Fore, Style

def is_directory_empty(directory):
    try:
        return not any(os.listdir(directory))
    except (PermissionError, FileNotFoundError):
        return False

def save_skipped_item(item_name):
    with open("skipped_files.txt", "a") as file:
        file.write(item_name + "\n")

def load_skipped_items():
    skipped_items = set()
    if os.path.exists("skipped_files.txt"):
        with open("skipped_files.txt", "r") as file:
            skipped_items = set(line.strip() for line in file)
    return skipped_items

def delete_unwanted_files(folder_path):
    # Initialize counters
    found_count = 0
    deleted_count = 0

    # Undesired directory names and files
    undesired_dirs = ["javax", "shadersmod", "viamcp", "META-INF", "google", "joptsimple", "tv", "oshi", "ibm", "sun", "iaversion", "mojang", "jcraft", "jhlabs", "apache", "json", "lwjgl", "yaml", "netty", "mcp", "ibxm", "javafx", "netscape", "mcupdater", "i", "spongepowered"]
    undesired_files = ["Start.java", "pack.png", ".DS_Store", "log4j2.xml", "InjectionAPI.java", "desktop.ini", "ShaderBackgroundApi3.java", "TestShader.ls"]

    # Load skipped items from the text file
    skipped_items = load_skipped_items()

    # Walk through the directory tree
    for root, dirs, files in os.walk(folder_path):
        for file in files:
            file_path = os.path.join(root, file)

            # Check if the file matches the criteria
            if file in undesired_files and file not in skipped_items:
                print(f"{Fore.YELLOW}Found file: {file_path}{Style.RESET_ALL}")
                found_count += 1

                # Ask for user confirmation before deletion
                user_input = input(f"{Fore.LIGHTGREEN_EX}Do you want to delete this file? (y/n): {Style.RESET_ALL}").lower()
                if user_input == "y":
                    os.remove(file_path)
                    deleted_count += 1
                    print(f"{Fore.GREEN}Deleted file: {file_path}{Style.RESET_ALL}")
                elif user_input == "n":
                    save_skipped_item(file)
                    print(f"{Fore.BLUE}Skipped file: {file_path}{Style.RESET_ALL}")

        for dir_name in dirs:
            dir_path = os.path.join(root, dir_name)

            # Check if the entire directory name is in the undesired directory names
            if dir_name in undesired_dirs and dir_path not in skipped_items:
                print(f"{Fore.YELLOW}Found directory: {dir_path}{Style.RESET_ALL}")
                found_count += 1

                # Check if the directory is empty
                if is_directory_empty(dir_path):
                    # Ask for user confirmation before deletion
                    user_input = input(f"{Fore.MAGENTA}Do you want to delete this empty directory? (y/n): {Style.RESET_ALL}").lower()
                    if user_input == "y":
                        os.rmdir(dir_path)
                        print(f"{Fore.GREEN}Deleted empty directory: {dir_path}{Style.RESET_ALL}")
                        deleted_count += 1
                    else:
                        print(f"{Fore.BLUE}Skipped empty directory: {dir_path}{Style.RESET_ALL}")
                else:
                    # Ask for user confirmation before deletion
                    user_input = input(f"{Fore.CYAN}Do you want to delete this directory and its contents? (y/n): {Style.RESET_ALL}").lower()
                    if user_input == "y":
                        for root_dir, _, files in os.walk(dir_path, topdown=False):
                            for file_name in files:
                                file_path = os.path.join(root_dir, file_name)
                                os.remove(file_path)
                                print(f"{Fore.GREEN}Deleted file: {file_path}{Style.RESET_ALL}")
                            os.rmdir(root_dir)
                            print(f"{Fore.GREEN}Deleted directory: {root_dir}{Style.RESET_ALL}")
                        deleted_count += 1
                    elif user_input == "n":
                        save_skipped_item(dir_path)
                        print(f"{Fore.BLUE}Skipped directory: {dir_path}{Style.RESET_ALL}")

    print(f"\nFound {found_count} files and directories matching the criteria.")
    print(f"Deleted {deleted_count} files and directories.")

if __name__ == "__main__":
    # Get the current script's directory
    script_directory = os.path.dirname(os.path.realpath(__file__))

    # Call the function with the script's directory
    delete_unwanted_files(script_directory)
