 # DreamDiffusion

DreamDiffusion is a Spigot plugin that allows players to generate AI art directly in Minecraft! By using the `/dream` command, players can create unique images based on their prompts and display them in-game using maps and item frames.

## Features

- **Inventory Requirement**: Players need to have an empty map item in their inventory to use the `/dream` command.
- **Generation Status**: A BossBar displays the status and queue position of the image generation process.
- **Configurable Settings**: Image generation settings and language can be customized.
- **Automatic Cleanup**: Maps are automatically deleted from ImageFrame when they despawn or are destroyed by fire, lava, or cacti.
- **Admin Controls**: Admins can limit the number of images a user can create by configuring ImageFrames group limits.
- **Download Links**: Players receive a link to the full-resolution image in chat for downloading.
- **High Performance**: Non-blocking operations ensure that the server and clients do not experience slowdowns.

## Future Features

- **VaultAPI Economy Support**: Integration with VaultAPI for economy-based features.
- **TabCompletes**: Autocomplete support for users, including image dimensions and possibly other generation options.
- **Configurable Concurrency**: Permissions-based concurrency settings for image generation.

## Installation

1. **Download the Plugin**: Download the latest version of DreamDiffusion from [here](https://github.com/CozmycDev/DreamDiffusion-Spigot/releases).
2. **Install ImageFrame**: Download and install the ImageFrame plugin by Loohp, which is required for managing and displaying image maps. You can find it [here](https://www.spigotmc.org/resources/imageframe-load-images-on-maps-item-frames-support-gifs-map-markers-survival-friendly.106031/).
3. **Place in Plugins Folder**: Move both DreamDiffusion and ImageFrame `.jar` files into your server's `plugins` folder.
4. **Restart Server**: Restart your Minecraft server to load the plugins.

## Usage

1. **Generate AI Art**: Players can use the `/dream <prompt>` command to generate AI art. Make sure you have an empty map item in your inventory.
2. **View Generation Status**: The BossBar will display the current status and queue position of the image generation.
3. **Retrieve Image**: Once the image is generated, place the given item in an item frame to display it.
4. **Download Image**: Click the link sent in chat to download the full-resolution image.

## Configuration

The configuration files allow you to customize various settings such as image generation parameters and language.

## Contribution

Contributions are welcome! Feel free to fork the repository, submit pull requests, or report issues.

## Dependencies

- **Stable Horde**: DreamDiffusion utilizes Stable Horde for AI-powered image generation. Learn more about Stable Horde at [aihorde.net](https://aihorde.net/).
- **ImageFrame by Loohp**: Essential for managing and displaying the generated images. Download it [here](https://www.spigotmc.org/resources/imageframe-load-images-on-maps-item-frames-support-gifs-map-markers-survival-friendly.106031/).

## License

DreamDiffusion is licensed under the GNU General Public License v3.0. See the [LICENSE](LICENSE) file for details.

## Contact

For support or inquiries, please open an issue on the GitHub repository or reach out to the development team via the SpigotMC page.

---

Enjoy creating and displaying AI-generated art in your Minecraft world!
