variable "token" {
  description = "Digital Ocean Access Token"
}

variable "ssh_fingerprint" {
  description = "id_rsa_do_token fingerprint"
}

variable "pub_key" {
	description = "id_rsa_do_token public key"
}

variable "pvt_key" {
  description = "id_rsa_do_token private key"
}

variable "region" {
  description = "Digital Ocean region"
  default = "ams2"
}

variable "rest_api_droplet_size" {
  description = "rngr-rest-api droplet size"
  default = "1gb"
}

variable "lb_droplet_size" {
  description = "lb size"
  default = "1gb"
}
