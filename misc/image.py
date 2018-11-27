from PIL import Image, ImageFont, ImageDraw, ImageEnhance

source_img = Image.open("rec.png").convert("RGBA")

card_color = "green"
color = "G"

# for x in range(1, 14):
#     draw = ImageDraw.Draw(source_img)
#     draw.rectangle(((0, 00), (125, 153)), fill="white")
#     if (x > 9):
#         draw.text((33-27, 57-16), color + str(x), fill=card_color, font=ImageFont.truetype("arial.ttf", 60))
#     else:
#         draw.text((43-24, 57-16), color + str(x), fill=card_color, font=ImageFont.truetype("arial.ttf", 60))
#
#     source_img.save(color + str(x) + ".png", "PNG")

draw = ImageDraw.Draw(source_img)
draw.rectangle(((0, 00), (125, 153)), fill="white")
draw.text((43, 57-16), "J", fill="black", font=ImageFont.truetype("arial.ttf", 60))
source_img.save("J.png", "PNG")
