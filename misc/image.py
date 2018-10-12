from PIL import Image, ImageFont, ImageDraw, ImageEnhance

source_img = Image.open("rec.png").convert("RGBA")

card_color = "orange"
color = "O"

for x in range(1, 14):
    draw = ImageDraw.Draw(source_img)
    draw.rectangle(((0, 00), (125, 153)), fill="white")
    if (x > 9):
        draw.text((33, 57), color + str(x), fill=card_color, font=ImageFont.truetype("arial.ttf", 32))
    else:
        draw.text((43, 57), color + str(x), fill=card_color, font=ImageFont.truetype("arial.ttf", 32))

    source_img.save(color + str(x) + ".png", "PNG")

draw = ImageDraw.Draw(source_img)
draw.rectangle(((0, 00), (125, 153)), fill="white")
draw.text((43, 57), color + "J", fill=card_color, font=ImageFont.truetype("arial.ttf", 32))
source_img.save(color + "J.png", "PNG")
