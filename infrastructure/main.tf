provider "digitalocean" {
  token = "${var.token}"
}

resource "digitalocean_droplet" "app-rest-api" {
  count = 1
  image = "ubuntu-14-04-x64"
  name = "app-rest-api"
  region = "${var.region}"
  size = "1gb"
  ssh_keys = ["${var.ssh_fingerprint}"]
}
